package com.horeca.site.services;

import com.horeca.site.models.Price;
import com.horeca.site.models.guest.Guest;
import com.horeca.site.models.hotel.services.AvailableServices;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

@Service
@Transactional
public class ReportGeneratorService {

    @Autowired
    private StayService stayService;

    @Autowired
    private ReportToHtmlService reportToHtmlService;

    public Report generateReport(String pin) {
        Stay stay = stayService.get(pin);
        AvailableServices availableServices = stay.getHotel().getAvailableServices();
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
            BigDecimal usageFee = priceToValue(availableServices.getBreakfast().getPrice());
            String usageFeeText = getUsageFeeText(usageFee, hotelCurrency);
            Pair<List<ReportOrder>, BigDecimal> reportsAndTotal = getBreakfastReportsAndTotal(orders.getBreakfastOrders(),
                    hotelCurrency);

            if (!Objects.equals(usageFee, BigDecimal.ZERO) || !reportsAndTotal.getLeft().isEmpty()) {
                ChargeDetails chargeDetails = new ChargeDetails("Breakfast", usageFeeText, reportsAndTotal.getLeft());
                chargeDetailsList.add(chargeDetails);

                totalAmount = totalAmount.add(usageFee);
                totalAmount = totalAmount.add(reportsAndTotal.getRight());
            }
        }

        /*
            Car park
         */
        if (!orders.getCarParkOrders().isEmpty()) {
            BigDecimal usageFee = priceToValue(availableServices.getCarPark().getPrice());
            String usageFeeText = getUsageFeeText(usageFee, hotelCurrency);

            if (!Objects.equals(usageFee, BigDecimal.ZERO)) {
                ChargeDetails chargeDetails = new ChargeDetails("Car park", usageFeeText, Collections.emptyList());
                chargeDetailsList.add(chargeDetails);

                totalAmount = totalAmount.add(usageFee);
            }
        }

        /*
            Room service
         */
        if (!orders.getRoomServiceOrders().isEmpty()) {
            BigDecimal usageFee = priceToValue(availableServices.getRoomService().getPrice());
            String usageFeeText = getUsageFeeText(usageFee, hotelCurrency);
            Pair<List<ReportOrder>, BigDecimal> reportsAndTotal = getRoomServiceReportsAndTotal(orders.getRoomServiceOrders(),
                    hotelCurrency);

            if (!Objects.equals(usageFee, BigDecimal.ZERO) || !reportsAndTotal.getLeft().isEmpty()) {
                ChargeDetails chargeDetails = new ChargeDetails("Room service", usageFeeText, reportsAndTotal.getLeft());
                chargeDetailsList.add(chargeDetails);

                totalAmount = totalAmount.add(usageFee);
                totalAmount = totalAmount.add(reportsAndTotal.getRight());
            }
        }

        /*
            Pet care
         */
        if (!orders.getPetCareOrders().isEmpty()) {
            BigDecimal usageFee = priceToValue(availableServices.getPetCare().getPrice());
            String usageFeeText = getUsageFeeText(usageFee, hotelCurrency);
            Pair<List<ReportOrder>, BigDecimal> reportsAndTotal = getPetCareReportsAndTotal(orders.getPetCareOrders(),
                    hotelCurrency);

            if (!Objects.equals(usageFee, BigDecimal.ZERO) || !reportsAndTotal.getLeft().isEmpty()) {
                ChargeDetails chargeDetails = new ChargeDetails("Pet care", usageFeeText, reportsAndTotal.getLeft());
                chargeDetailsList.add(chargeDetails);

                totalAmount = totalAmount.add(usageFee);
                totalAmount = totalAmount.add(reportsAndTotal.getRight());
            }
        }

        /*
            Taxi
         */
        if (!orders.getTaxiOrders().isEmpty()) {
            BigDecimal usageFee = priceToValue(availableServices.getTaxi().getPrice());
            String usageFeeText = getUsageFeeText(usageFee, hotelCurrency);

            if (!Objects.equals(usageFee, BigDecimal.ZERO)) {
                ChargeDetails chargeDetails = new ChargeDetails("Taxi", usageFeeText, Collections.emptyList());
                chargeDetailsList.add(chargeDetails);

                totalAmount = totalAmount.add(usageFee);
            }
        }

        /*
            Housekeeping
         */
        if (!orders.getHousekeepingOrders().isEmpty()) {
            BigDecimal usageFee = priceToValue(availableServices.getHousekeeping().getPrice());
            String usageFeeText = getUsageFeeText(usageFee, hotelCurrency);

            if (!Objects.equals(usageFee, BigDecimal.ZERO)) {
                ChargeDetails chargeDetails = new ChargeDetails("Housekeeping", usageFeeText, Collections.emptyList());
                chargeDetailsList.add(chargeDetails);

                totalAmount = totalAmount.add(usageFee);
            }
        }

        /*
            Bar
         */
        if (!orders.getBarOrders().isEmpty()) {
            BigDecimal usageFee = priceToValue(availableServices.getBar().getPrice());
            String usageFeeText = getUsageFeeText(usageFee, hotelCurrency);
            Pair<List<ReportOrder>, BigDecimal> reportsAndTotal = getBarReportsAndTotal(orders.getBarOrders(),
                    hotelCurrency);

            if (!Objects.equals(usageFee, BigDecimal.ZERO) || !reportsAndTotal.getLeft().isEmpty()) {
                ChargeDetails chargeDetails = new ChargeDetails("Bar", usageFeeText, reportsAndTotal.getLeft());
                chargeDetailsList.add(chargeDetails);

                totalAmount = totalAmount.add(usageFee);
                totalAmount = totalAmount.add(reportsAndTotal.getRight());
            }
        }

        /*
            Rental
         */
        if (!orders.getRentalOrders().isEmpty()) {
            BigDecimal usageFee = priceToValue(availableServices.getRental().getPrice());
            String usageFeeText = getUsageFeeText(usageFee, hotelCurrency);
            Pair<List<ReportOrder>, BigDecimal> reportsAndTotal = getRentalReportsAndTotal(orders.getRentalOrders(),
                    hotelCurrency);

            if (!Objects.equals(usageFee, BigDecimal.ZERO) || !reportsAndTotal.getLeft().isEmpty()) {
                ChargeDetails chargeDetails = new ChargeDetails("Rental", usageFeeText, reportsAndTotal.getLeft());
                chargeDetailsList.add(chargeDetails);

                totalAmount = totalAmount.add(usageFee);
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

            ReportOrder reportOrder = new ReportOrder(description, amount);
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

            ReportOrder reportOrder = new ReportOrder(description, amount);
            reportOrders.add(reportOrder);
        }

        return new ImmutablePair<>(reportOrders, totalAmount);
    }

    private static Pair<List<ReportOrder>, BigDecimal> getPetCareReportsAndTotal(Collection<PetCareOrder> orders,
                                                                                 String hotelCurrency) {
        List<ReportOrder> reportOrders = new ArrayList<>();
        BigDecimal totalAmount = BigDecimal.ZERO;

        for (PetCareOrder order : orders) {
            String description = order.getItem().getName();
            String amount = priceToValue(order.getItem().getPrice()) + " " + hotelCurrency;

            ReportOrder reportOrder = new ReportOrder(description, amount);
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

            ReportOrder reportOrder = new ReportOrder(description, amount);
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

            ReportOrder reportOrder = new ReportOrder(description, amount);
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

    private static String getUsageFeeText(BigDecimal fee, String hotelCurrency) {
        if (!Objects.equals(fee, BigDecimal.ZERO)) {
            return fee + " " + hotelCurrency;
        }
        return null;
    }
}
