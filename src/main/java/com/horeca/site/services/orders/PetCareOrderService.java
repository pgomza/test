package com.horeca.site.services.orders;

import com.horeca.site.exceptions.ResourceNotFoundException;
import com.horeca.site.models.hotel.services.AvailableServiceType;
import com.horeca.site.models.hotel.services.petcare.PetCare;
import com.horeca.site.models.hotel.services.petcare.PetCareItem;
import com.horeca.site.models.notifications.NewOrderEvent;
import com.horeca.site.models.orders.OrderStatus;
import com.horeca.site.models.orders.Orders;
import com.horeca.site.models.orders.petcare.PetCareOrder;
import com.horeca.site.models.orders.petcare.PetCareOrderPOST;
import com.horeca.site.models.stay.Stay;
import com.horeca.site.repositories.orders.PetCareOrderRepository;
import com.horeca.site.services.services.StayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@Transactional
public class PetCareOrderService extends GenericOrderService<PetCareOrder> {

    @Autowired
    private OrdersService ordersService;

    @Autowired
    private StayService stayService;

    @Autowired
    private PetCareOrderRepository repository;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @Override
    protected CrudRepository<PetCareOrder, Long> getRepository() {
        return repository;
    }

    public Set<PetCareOrder> getAll(String stayPin) {
        Orders orders = ordersService.get(stayPin);
        return orders.getPetCareOrders();
    }

    public PetCareOrder add(String stayPin, PetCareOrderPOST entity) {
        PetCareOrder newOrder = new PetCareOrder();

        PetCareItem resolvedItem = resolveItemIdToEntity(stayPin, entity.getItemId());

        newOrder.setStatus(OrderStatus.NEW);
        newOrder.setDate(entity.getDate());
        newOrder.setItem(resolvedItem);
        PetCareOrder savedOrder = repository.save(newOrder);

        Stay stay = stayService.get(stayPin);
        Set<PetCareOrder> petCareOrders = stay.getOrders().getPetCareOrders();
        petCareOrders.add(savedOrder);
        stayService.update(stayPin, stay);

        return savedOrder;
    }

    public PetCareOrder addAndTryToNotify(String stayPin, PetCareOrderPOST entity) {
        PetCareOrder added = add(stayPin, entity);
        Stay stay = stayService.get(stayPin);

        eventPublisher.publishEvent(new NewOrderEvent(this, AvailableServiceType.PETCARE, stay));

        return added;
    }

    private PetCareItem resolveItemIdToEntity(String stayPin, Long id) {
        PetCare petCare = stayService.get(stayPin).getHotel().getAvailableServices().getPetCare();
        for (PetCareItem item : petCare.getItems()) {
            if (item.getId().equals(id))
                return item;
        }

        throw new ResourceNotFoundException("Could not find an item with such an id");
    }
}
