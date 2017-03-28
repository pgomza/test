package com.horeca.site.extractors;

import com.horeca.site.models.hotel.Hotel;
import com.horeca.site.services.HotelService;
import org.apache.commons.dbcp.BasicDataSource;
import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class HotelDataExtractor {

    private Pattern reviewsPattern = Pattern.compile("<li>([^<]+)</li>");

    private BasicDataSource dataSource = null;
    private JdbcTemplate jdbcTemplate = null;
    private static final String DATASOURCE_URL = "jdbc:mysql://localhost/hoteldata?useUnicode=yes&characterEncoding=UTF-8";

    @Autowired
    private HotelService hotelService;

    @PostConstruct
    private void initDataSource() {
    }

    public void extract(int limit) {
        List<HotelData> hotelList = jdbcTemplate.query(
                "select * from hotel h, property_type p, chain c " +
                "where h.property_type_id = p.id " +
                "and h.chain_id = c.id limit " + limit,
                new HotelMapper()
        );

        int i = 1;
        List<Hotel> convertedHotels = new ArrayList<>();
        for (HotelData hotelData : hotelList) {
            convertedHotels.add(hotelService.convertFromHotelData(hotelData));

            if (i % 100 == 0)
                System.out.println("iterating over hoteldata, i: " + i);
            i++;
        }

        hotelService.addAll(convertedHotels);
    }

    private static String getNullIfEmpty(String text) {
        if (text != null) {
            if (text.isEmpty())
                return null;
            else
                return text;
        }
        return null;
    }

    private static Double getNullIfZero(Double value) {
        if (value != null) {
            if (Double.compare(value, 0.0) == 0)
                return null;
            else
                return value;
        }
        return null;
    }

    private static Float getNullIfZero(Float value) {
        if (value != null) {
            if (Float.compare(value, 0.0f) == 0)
                return null;
            else
                return value;
        }
        return null;
    }

    private static Integer getNullIfZero(Integer value) {
        if (value != null) {
            if (Integer.compare(value, 0) == 0)
                return null;
            else
                return value;
        }
        return null;
    }

    public static class HotelData {

        public Long id;
        public String title;
        public String addressFull;
        public String email;
        public String website;
        public String phone;
        public String fax;
        public Double longitude;
        public Double latitude;
        public Float starRating;
        public Integer rooms;
        public Integer priceUSD;
        public LocalTime checkIn;
        public LocalTime checkOut;
        public Float ratingOverall;
        public String ratingOverallText;
        public Float ratingCleanliness;
        public Float ratingDining;
        public Float ratingFacilities;
        public Float ratingLocation;
        public Float ratingRooms;
        public Float ratingService;
        public String ratingPoints;
        public Integer reviewsCount;
        public String reviewsSummaryPositive;
        public String reviewsSummaryNegative;
        public String description;
        public String propertyType;
        public String chain;
    }

    private static class HotelMapper implements RowMapper<HotelData> {

        private DateTimeFormatter formatter = DateTimeFormat.forPattern("h:mm a");
        private Pattern timePattern = Pattern.compile("\\d{1,2}:\\d{2}\\s?(AM|PM)");

        @Override
        public HotelData mapRow(ResultSet rs, int rowNum) throws SQLException {
            HotelData hotel = new HotelData();
            hotel.id = rs.getLong("h.id");
            hotel.title = rs.getString("h.title");
            hotel.addressFull = getNullIfEmpty(rs.getString("h.address_full"));
            hotel.email = getNullIfEmpty(rs.getString("h.email"));
            hotel.website = getNullIfEmpty(rs.getString("h.website"));
            hotel.phone = getNullIfEmpty(rs.getString("h.phone"));
            hotel.fax = getNullIfEmpty(rs.getString("h.fax"));
            hotel.longitude = getNullIfZero(rs.getDouble("h.longitude"));
            hotel.latitude = getNullIfZero(rs.getDouble("h.latitude"));
            hotel.starRating = getNullIfZero(rs.getFloat("h.star_rating"));
            hotel.rooms = getNullIfZero(rs.getInt("h.rooms"));
            hotel.priceUSD = getNullIfZero(rs.getInt("h.price_usd"));

            String checkIn = rs.getString("h.check_in");
            if (checkIn != null) {
                Matcher checkInMatcher = timePattern.matcher(checkIn);
                if (checkInMatcher.find()) {
                    String time = checkInMatcher.group();
                    if (time.equals("0:00 AM"))
                        time = "12:00 AM";
                    else if (time.equals("0:00 PM"))
                        time = "12:00 PM";

                    hotel.checkIn = formatter.parseLocalTime(time);
                }
            }

            String checkOut = rs.getString("h.check_out");
            if (checkOut != null) {
                Matcher checkOutMatcher = timePattern.matcher(checkOut);
                if (checkOutMatcher.find()) {
                    String time = checkOutMatcher.group();
                    if (time.equals("0:00 AM"))
                        time = "12:00 AM";
                    else if (time.equals("0:00 PM"))
                        time = "12:00 PM";

                    hotel.checkOut = formatter.parseLocalTime(time);
                }
            }

            hotel.ratingOverall = getNullIfZero(rs.getFloat("h.rating_overall"));
            hotel.ratingOverallText = getNullIfEmpty(rs.getString("h.rating_overall_text"));
            hotel.ratingCleanliness = getNullIfZero(rs.getFloat("h.rating_cleanliness"));
            hotel.ratingDining = getNullIfZero(rs.getFloat("h.rating_dining"));
            hotel.ratingFacilities = getNullIfZero(rs.getFloat("h.rating_facilities"));
            hotel.ratingLocation = getNullIfZero(rs.getFloat("h.rating_location"));
            hotel.ratingRooms = getNullIfZero(rs.getFloat("h.rating_rooms"));
            hotel.ratingService = getNullIfZero(rs.getFloat("h.rating_service"));
            hotel.ratingPoints = getNullIfEmpty(rs.getString("h.rating_points"));
            hotel.reviewsCount = getNullIfZero(rs.getInt("h.reviews_count"));
            hotel.reviewsSummaryPositive = getNullIfEmpty(rs.getString("h.reviews_summary_positive"));
            hotel.reviewsSummaryNegative = getNullIfEmpty(rs.getString("h.reviews_summary_negative"));
            hotel.description = getNullIfEmpty(rs.getString("h.description"));
            hotel.propertyType = getNullIfEmpty(rs.getString("p.title"));

            String chain = rs.getString("c.title");
            if (!chain.equals("(No Chain)"))
                hotel.chain = chain;

            return hotel;
        }
    }
}
