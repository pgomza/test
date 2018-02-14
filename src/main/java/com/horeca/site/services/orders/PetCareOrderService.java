package com.horeca.site.services.orders;

import com.horeca.site.exceptions.ResourceNotFoundException;
import com.horeca.site.models.hotel.services.AvailableServiceType;
import com.horeca.site.models.hotel.services.petcare.PetCare;
import com.horeca.site.models.hotel.services.petcare.PetCareItem;
import com.horeca.site.models.orders.OrderStatus;
import com.horeca.site.models.orders.Orders;
import com.horeca.site.models.orders.petcare.PetCareOrder;
import com.horeca.site.models.orders.petcare.PetCareOrderPOST;
import com.horeca.site.models.stay.Stay;
import com.horeca.site.repositories.orders.PetCareOrderRepository;
import com.horeca.site.services.services.StayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@Transactional
public class PetCareOrderService extends GenericOrderService<PetCareOrder> {

    private OrdersService ordersService;

    @Autowired
    public PetCareOrderService(ApplicationEventPublisher eventPublisher,
                               PetCareOrderRepository repository,
                               StayService stayService,
                               OrdersService ordersService) {
        super(eventPublisher, repository, stayService);
        this.ordersService = ordersService;
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

    public PetCareOrder addAndNotify(String stayPin, PetCareOrderPOST entity) {
        PetCareOrder added = add(stayPin, entity);
        notifyAboutNewOrder(stayPin, AvailableServiceType.PETCARE);

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
