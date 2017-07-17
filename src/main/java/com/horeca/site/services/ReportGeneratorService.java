package com.horeca.site.services;

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
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@Transactional
public class ReportGeneratorService {

    @Autowired
    private StayService stayService;

    public Report generateReport(String pin) {
        Stay stay = stayService.get(pin);
        AvailableServices availableServices = stay.getHotel().getAvailableServices();
        String hotelCurrency = stay.getHotel().getCurrency().toString();

        List<ChargeDetails> chargeDetailsList = new ArrayList<>();
        BigDecimal totalAmount = BigDecimal.ZERO;

        Guest guest = stay.getGuest();
        String guestName = guest.getFirstName() + " " + guest.getLastName();
        String roomNumber = stay.getRoomNumber();
        ReportGuest reportGuest = new ReportGuest(guestName, roomNumber);

        Orders orders = stay.getOrders();

        if (!orders.getBreakfastOrders().isEmpty()) {
            Price usageFee = availableServices.getBreakfast().getPrice();
            String usageFeeText = priceToText(usageFee);
            Pair<List<ReportOrder>, BigDecimal> reportsAndTotal = getReportsAndTotal(orders.getBreakfastOrders());

            ChargeDetails chargeDetails = new ChargeDetails("Bar", usageFeeText, reportsAndTotal.getLeft());
            chargeDetailsList.add(chargeDetails);

            totalAmount = totalAmount.add(priceToValue(usageFee));
            totalAmount = totalAmount.add(reportsAndTotal.getRight());
        }

        return new Report(reportGuest, chargeDetailsList, totalAmount + " " + hotelCurrency);
    }

    private static Pair<List<ReportOrder>, BigDecimal> getReportsAndTotal(Collection<BreakfastOrder> orders) {
        List<ReportOrder> reportOrders = new ArrayList<>();
        BigDecimal totalAmount = BigDecimal.ZERO;

        for (BreakfastOrder order : orders) {
            List<String> descriptionList = new ArrayList<>();
            for (BreakfastOrderItem orderItem : order.getItems()) {
                if (orderItem.getCount() > 1) {
                    descriptionList.add(orderItem.getItem().getName() + " (" + orderItem.getCount() + "x)");
                }
                else {
                    descriptionList.add(orderItem.getItem().getName());
                }

                if (orderItem.getItem().getPrice().getValue() != null) {
                    totalAmount = totalAmount.add(
                            orderItem.getItem().getPrice().getValue().multiply(new BigDecimal(orderItem.getCount()))
                    );
                }
            }
            String description = String.join(", ", descriptionList);
            String amount = priceToText(order.getTotal());

            ReportOrder reportOrder = new ReportOrder(description, amount);
            reportOrders.add(reportOrder);
        }

        return new ImmutablePair<>(reportOrders, totalAmount);
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

    private static BigDecimal priceToValue(Price price) {
        if (price.getValue() == null) {
            return BigDecimal.ZERO;
        }
        return price.getValue();
    }
}
