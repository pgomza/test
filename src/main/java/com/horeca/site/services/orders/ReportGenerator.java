package com.horeca.site.services.orders;

import com.horeca.site.models.Price;
import com.horeca.site.models.guest.Guest;
import com.horeca.site.models.hotel.services.AvailableServices;
import com.horeca.site.models.orders.Orders;
import com.horeca.site.models.orders.breakfast.BreakfastOrder;
import com.horeca.site.models.orders.breakfast.BreakfastOrderItem;
import com.horeca.site.models.orders.report.ChargeDetails;
import com.horeca.site.models.orders.report.Report;
import com.horeca.site.models.orders.report.ReportGuest;
import com.horeca.site.models.orders.report.ReportOrder;
import com.horeca.site.models.stay.Stay;
import com.horeca.site.services.services.StayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@Transactional
public class ReportGenerator {

    @Autowired
    private StayService stayService;

    public Report generateReport(String pin) {
        Stay stay = stayService.get(pin);
        AvailableServices availableServices = stay.getHotel().getAvailableServices();

        Guest guest = stay.getGuest();
        String guestName = guest.getFirstName() + " " + guest.getLastName();
        String roomNumber = stay.getRoomNumber();
        ReportGuest reportGuest = new ReportGuest(guestName, roomNumber);

        List<ChargeDetails> chargeDetailsList = new ArrayList<>();
        BigDecimal totalAmount = BigDecimal.ZERO;
        Orders orders = stay.getOrders();

        if (!orders.getBreakfastOrders().isEmpty()) {
            String usageFee = priceToText(availableServices.getBreakfast().getPrice());
            ChargeDetails chargeDetails =
                    new ChargeDetails("Bar", usageFee, getForBreakfast(orders.getBreakfastOrders()));
            chargeDetailsList.add(chargeDetails);
        }

        /*
            the rest of the orders
         */

        return null;
    }

    private static List<ReportOrder> getForBreakfast(Collection<BreakfastOrder> orders) {
        List<ReportOrder> result = new ArrayList<>();
        for (BreakfastOrder order : orders) {
            List<String> descriptionList = new ArrayList<>();
            for (BreakfastOrderItem orderItem : order.getItems()) {
                if (orderItem.getCount() > 1) {
                    descriptionList.add(orderItem.getItem().getName() + " (" + orderItem.getCount() + "x)");
                }
                else {
                    descriptionList.add(orderItem.getItem().getName());
                }
            }
            String description = String.join(", ", descriptionList);
            String amount = priceToText(order.getTotal());

            ReportOrder reportOrder = new ReportOrder(description, amount);
            result.add(reportOrder);
        }

        return result;
    }

    private static String priceToText(Price price) {
        String text = price.getText();
        if ("Free".equalsIgnoreCase(text)) {
            return "0" + price.getCurrency();
        }
        else {
            return price.getValue() + "" + price.getCurrency();
        }
    }
}
