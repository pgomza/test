package com.horeca.site.services;

import com.horeca.site.models.orders.report.ChargeDetails;
import com.horeca.site.models.orders.report.Report;
import com.horeca.site.models.orders.report.ReportGuest;
import com.horeca.site.models.orders.report.ReportOrder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReportToHtmlService {

    public String convert(Report report) {
        StringBuilder htmlBuilder = new StringBuilder();

        htmlBuilder.append( "<!DOCTYPE html>\n" +
                            "<html lang=\"en\">\n");

        htmlBuilder.append(getHead());

        htmlBuilder.append("<body>\n");

        ReportGuest guest = report.getReportGuest();
        htmlBuilder.append(getGuest(guest.getName(), guest.getRoomNumber(), guest.getArrival(), guest.getDeparture()));

        if (!report.getChargeDetails().isEmpty()) {
            htmlBuilder.append("<div class=\"orders\">\n" +
                    "        <div class=\"header\">Placed orders</div>\n" +
                    "        <table>\n" +
                    "            <tr>\n" +
                    "                <th>Service</th>\n" +
                    "                <th>Details</th>\n" +
                    "                <th>Amount</th>\n" +
                    "            </tr>\n");

            htmlBuilder.append(getOrders(report.getChargeDetails()));

            htmlBuilder.append("<tr>\n" +
                    "                <td class=\"last-row\"></td>\n" +
                    "                <td class=\"last-row total\">Total</td>\n" +
                    "                <td>" + report.getTotalAmount() + "</td>\n" +
                    "            </tr>\n" +
                    "        </table>\n" +
                    "    </div>\n");
        }
        else {
            htmlBuilder.append( "<div class=\"orders\">\n" +
                    "               <div class=\"header\">There are no orders for this stay</div>\n" +
                    "           </div>\n");
        }

        htmlBuilder.append("</body>\n" +
                "</html>\n");

        return htmlBuilder.toString();
    }

    private String getHead() {
        return "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <title>Template</title>\n" +
                "    <style>\n" +
                "        body {\n" +
                "            margin: 20px;\n" +
                "        }\n" +
                "\n" +
                "        .header {\n" +
                "            font-size: large;\n" +
                "            font-weight: bold;\n" +
                "            text-transform: uppercase;\n" +
                "            padding-bottom: 15px;\n" +
                "        }\n" +
                "\n" +
                "        .guest {\n" +
                "            margin-bottom: 40px;\n" +
                "        }\n" +
                "\n" +
                "        .guest .entry {\n" +
                "            padding-bottom: 5px;\n" +
                "        }\n" +
                "\n" +
                "        .guest .key {\n" +
                "            width: 120px;\n" +
                "            display: inline-block;\n" +
                "            font-weight: bold;\n" +
                "        }\n" +
                "\n" +
                "        .guest .value {\n" +
                "            display: inline-block;\n" +
                "        }\n" +
                "\n" +
                "        table {\n" +
                "            font-family: arial, sans-serif;\n" +
                "            border-collapse: collapse;\n" +
                "            width: 90%;\n" +
                "            text-align: center;\n" +
                "            margin: 0 auto;\n" +
                "            page-break-inside: auto;\n" +
                "        }\n" +
                "\n" +
                "       tr {" +
                "           page-break-inside: avoid;" +
                "           page-break-after: auto;" +
                "       }\n" +
                "\n" +
                "        tr:nth-child(even) {\n" +
                "            background-color: #F0F0F0;\n" +
                "        }\n" +
                "\n" +
                "        td, th {\n" +
                "            border: 1px solid #dddddd;\n" +
                "            padding: 8px;\n" +
                "        }\n" +
                "\n" +
                "        .orders .details {\n" +
                "            font-size: 14px;\n" +
                "        }\n" +
                "\n" +
                "        .orders .last-row {\n" +
                "            background-color: white;\n" +
                "            border: none;\n" +
                "        }\n" +
                "\n" +
                "        .orders .total {\n" +
                "            text-align: right;\n" +
                "            font-weight: bold;\n" +
                "        }\n" +
                "    </style>\n" +
                "</head>\n";
    }

    private String getGuest(String name, String roomNumber, String arrival, String departure) {
        return "<div class=\"guest\">\n" +
                "        <div class=\"header\">Information about the stay</div>\n" +
                "        <div class=\"entry\">\n" +
                "            <div class=\"key\">Guest's name:</div>\n" +
                "            <div class=\"value\">" + name + "</div>\n" +
                "        </div>\n" +
                "        <div class=\"entry\">\n" +
                "            <div class=\"key\">Room number:</div>\n" +
                "            <div class=\"value\">" + roomNumber + "</div>\n" +
                "        </div>\n" +
                "        <div class=\"entry\">\n" +
                "            <div class=\"key\">Arrival:</div>\n" +
                "            <div class=\"value\">" + arrival + "</div>\n" +
                "        </div>\n" +
                "        <div class=\"entry\">\n" +
                "            <div class=\"key\">Departure:</div>\n" +
                "            <div class=\"value\">" + departure + "</div>\n" +
                "        </div>\n" +
                "    </div>\n";
    }

    private String getOrders(List<ChargeDetails> chargeDetailsList) {
        StringBuilder ordersBuilder = new StringBuilder();
        for (ChargeDetails chargeDetails : chargeDetailsList) {
            ordersBuilder.append("<tr>\n" +
                    "                <td>" + chargeDetails.getServiceName() + "</td>\n" +
                    "                <td></td>\n" +
                    "                <td></td>\n" +
                    "            </tr>\n");

            for (ReportOrder order : chargeDetails.getReportOrders()) {
                ordersBuilder.append("<tr>\n" +
                        "                <td></td>\n" +
                        "                <td class=\"details\">" + order.getDescription() + "</td>\n" +
                        "                <td>" + order.getAmount() + "</td>\n" +
                        "            </tr>\n");
            }

            if (chargeDetails.getUsageFee() != null) {
                ordersBuilder.append("<tr>\n" +
                        "                <td></td>\n" +
                        "                <td class=\"details\">+ fee for using this service</td>\n" +
                        "                <td>" + chargeDetails.getUsageFee() + "</td>\n" +
                        "            </tr>\n");
            }
        }
        return ordersBuilder.toString();
    }
}
