package com.horeca.site.services;

import com.horeca.site.models.Price;
import com.horeca.site.models.guest.Guest;
import com.horeca.site.models.hotel.services.petcare.PetCareItem;
import com.horeca.site.models.orders.Orders;
import com.horeca.site.models.orders.bar.BarOrder;
import com.horeca.site.models.orders.bar.BarOrderItem;
import com.horeca.site.models.orders.breakfast.BreakfastOrder;
import com.horeca.site.models.orders.breakfast.BreakfastOrderItem;
import com.horeca.site.models.orders.petcare.PetCareOrder;
import com.horeca.site.models.orders.rental.RentalOrder;
import com.horeca.site.models.orders.rental.RentalOrderItem;
import com.horeca.site.models.orders.report.ChargeDetails;
import com.horeca.site.models.orders.report.Report;
import com.horeca.site.models.orders.report.ReportGuest;
import com.horeca.site.models.orders.report.ReportOrder;
import com.horeca.site.models.orders.roomservice.RoomServiceOrder;
import com.horeca.site.models.orders.roomservice.RoomServiceOrderItem;
import com.horeca.site.models.stay.Stay;
import com.horeca.site.services.services.StayService;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@Transactional
public class ReportGeneratorService {

    @Autowired
    private StayService stayService;

    @Autowired
    private ReportToHtmlService reportToHtmlService;

    public Report generateReport(String pin) {
        Stay stay = stayService.getWithoutCheckingStatus(pin);
        String hotelCurrency = stay.getHotel().getCurrency().toString();

        List<ChargeDetails> chargeDetailsList = new ArrayList<>();
        BigDecimal totalAmount = BigDecimal.ZERO;

        Guest guest = stay.getGuest();
        String guestName = guest.getFirstName() + " " + guest.getLastName();
        String roomNumber = stay.getRoomNumber();
        String arrival = stay.getFromDate().toString("dd-MM-yyyy");
        String departure = stay.getToDate().toString("dd-MM-yyyy");

        ReportGuest reportGuest = new ReportGuest(guestName, roomNumber, arrival, departure);

        Orders orders = stay.getOrders();

        /*
            Breakfast
         */
        if (!orders.getBreakfastOrders().isEmpty()) {
            Pair<List<ReportOrder>, BigDecimal> reportsAndTotal = getBreakfastReportsAndTotal(orders.getBreakfastOrders(),
                    hotelCurrency);

            if (!reportsAndTotal.getLeft().isEmpty()) {
                ChargeDetails chargeDetails = new ChargeDetails("Breakfast", reportsAndTotal.getLeft());
                chargeDetailsList.add(chargeDetails);

                totalAmount = totalAmount.add(reportsAndTotal.getRight());
            }
        }

        /*
            Room service
         */
        if (!orders.getRoomServiceOrders().isEmpty()) {
            Pair<List<ReportOrder>, BigDecimal> reportsAndTotal = getRoomServiceReportsAndTotal(orders.getRoomServiceOrders(),
                    hotelCurrency);

            if (!reportsAndTotal.getLeft().isEmpty()) {
                ChargeDetails chargeDetails = new ChargeDetails("Room service", reportsAndTotal.getLeft());
                chargeDetailsList.add(chargeDetails);

                totalAmount = totalAmount.add(reportsAndTotal.getRight());
            }
        }

        /*
            Pet care
         */
        if (!orders.getPetCareOrders().isEmpty()) {
            Pair<List<ReportOrder>, BigDecimal> reportsAndTotal = getPetCareReportsAndTotal(orders.getPetCareOrders(),
                    hotelCurrency);

            if (!reportsAndTotal.getLeft().isEmpty()) {
                ChargeDetails chargeDetails = new ChargeDetails("Pet care", reportsAndTotal.getLeft());
                chargeDetailsList.add(chargeDetails);

                totalAmount = totalAmount.add(reportsAndTotal.getRight());
            }
        }

        /*
            Bar
         */
        if (!orders.getBarOrders().isEmpty()) {
            Pair<List<ReportOrder>, BigDecimal> reportsAndTotal = getBarReportsAndTotal(orders.getBarOrders(),
                    hotelCurrency);

            if (!reportsAndTotal.getLeft().isEmpty()) {
                ChargeDetails chargeDetails = new ChargeDetails("Bar", reportsAndTotal.getLeft());
                chargeDetailsList.add(chargeDetails);

                totalAmount = totalAmount.add(reportsAndTotal.getRight());
            }
        }

        /*
            Rental
         */
        if (!orders.getRentalOrders().isEmpty()) {
            Pair<List<ReportOrder>, BigDecimal> reportsAndTotal = getRentalReportsAndTotal(orders.getRentalOrders(),
                    hotelCurrency);

            if (!reportsAndTotal.getLeft().isEmpty()) {
                ChargeDetails chargeDetails = new ChargeDetails("Rental", reportsAndTotal.getLeft());
                chargeDetailsList.add(chargeDetails);

                totalAmount = totalAmount.add(reportsAndTotal.getRight());
            }
        }

        return new Report(reportGuest, chargeDetailsList, totalAmount + " " + hotelCurrency);
    }

    public String generateReportInHtml(String pin) {
        Report report = generateReport(pin);
        return reportToHtmlService.convert(report);
    }

    private static Pair<List<ReportOrder>, BigDecimal> getBreakfastReportsAndTotal(Collection<BreakfastOrder> orders,
                                                                                   String hotelCurrency) {
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
            String amount = priceToValue(order.getTotal()) + " " + hotelCurrency;

            ReportOrder reportOrder = new ReportOrder(description, amount, timestampToString(order.getCreatedAt()));
            reportOrders.add(reportOrder);
        }

        return new ImmutablePair<>(reportOrders, totalAmount);
    }

    private static Pair<List<ReportOrder>, BigDecimal> getRoomServiceReportsAndTotal(Collection<RoomServiceOrder> orders,
                                                                                     String hotelCurrency) {
        List<ReportOrder> reportOrders = new ArrayList<>();
        BigDecimal totalAmount = BigDecimal.ZERO;

        for (RoomServiceOrder order : orders) {
            List<String> descriptionList = new ArrayList<>();
            for (RoomServiceOrderItem orderItem : order.getItems()) {
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
            String amount = priceToValue(order.getTotal()) + " " + hotelCurrency;

            ReportOrder reportOrder = new ReportOrder(description, amount, timestampToString(order.getCreatedAt()));
            reportOrders.add(reportOrder);
        }

        return new ImmutablePair<>(reportOrders, totalAmount);
    }

    private static Pair<List<ReportOrder>, BigDecimal> getPetCareReportsAndTotal(Collection<PetCareOrder> orders,
                                                                                 String hotelCurrency) {
        List<ReportOrder> reportOrders = new ArrayList<>();
        BigDecimal totalAmount = BigDecimal.ZERO;

        for (PetCareOrder order : orders) {
            PetCareItem item = order.getItem();
            String description = item.getName();
            String amount = priceToValue(item.getPrice()) + " " + hotelCurrency;

            ReportOrder reportOrder = new ReportOrder(description, amount, timestampToString(order.getCreatedAt()));

            totalAmount = totalAmount.add(item.getPrice().getValue());
            reportOrders.add(reportOrder);
        }

        return new ImmutablePair<>(reportOrders, totalAmount);
    }

    private static Pair<List<ReportOrder>, BigDecimal> getBarReportsAndTotal(Collection<BarOrder> orders,
                                                                             String hotelCurrency) {
        List<ReportOrder> reportOrders = new ArrayList<>();
        BigDecimal totalAmount = BigDecimal.ZERO;

        for (BarOrder order : orders) {
            List<String> descriptionList = new ArrayList<>();
            for (BarOrderItem orderItem : order.getItems()) {
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
            String amount = priceToValue(order.getTotal()) + " " + hotelCurrency;

            ReportOrder reportOrder = new ReportOrder(description, amount, timestampToString(order.getCreatedAt()));
            reportOrders.add(reportOrder);
        }

        return new ImmutablePair<>(reportOrders, totalAmount);
    }

    private static Pair<List<ReportOrder>, BigDecimal> getRentalReportsAndTotal(Collection<RentalOrder> orders,
                                                                                String hotelCurrency) {
        List<ReportOrder> reportOrders = new ArrayList<>();
        BigDecimal totalAmount = BigDecimal.ZERO;

        for (RentalOrder order : orders) {
            List<String> descriptionList = new ArrayList<>();
            for (RentalOrderItem orderItem : order.getItems()) {
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
            String amount = priceToValue(order.getTotal()) + " " + hotelCurrency;

            ReportOrder reportOrder = new ReportOrder(description, amount, timestampToString(order.getCreatedAt()));
            reportOrders.add(reportOrder);
        }

        return new ImmutablePair<>(reportOrders, totalAmount);
    }

    private static BigDecimal priceToValue(Price price) {
        if (price.getValue() == null) {
            return BigDecimal.ZERO;
        }
        return price.getValue();
    }

    private static String timestampToString(Timestamp timestamp) {
        DateTime dateTime = new DateTime(timestamp);
        return dateTime.toString("dd-MM-YYYY");
    }
}
