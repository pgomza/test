package com.horeca.site.extractors;

import com.horeca.site.models.hoteldata.HotelData;
import com.horeca.site.models.hoteldata.HotelFeature;
import com.horeca.site.models.hoteldata.HotelRatings;
import com.horeca.site.models.hoteldata.HotelReviews;
import com.horeca.site.repositories.hoteldata.HotelDataRepository;
import com.horeca.site.repositories.hoteldata.HotelFeatureRepository;
import org.apache.commons.dbcp.BasicDataSource;
import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${datasource.driverclassname.local}")
    private String localDataSourceDriverClassName;

    @Value("${datasource.username}")
    private String dataSourceUsername;

    @Value("${datasource.password}")
    private String dataSourcePassword;

    @Autowired
    private HotelFeatureRepository hotelFeatureRepository;

    @Autowired
    private HotelDataRepository hotelDataRepository;

    @PostConstruct
    private void initDataSource() {
        dataSource = new BasicDataSource();
        dataSource.setDriverClassName(localDataSourceDriverClassName);
        dataSource.setUrl(DATASOURCE_URL);
        dataSource.setUsername(dataSourceUsername);
        dataSource.setPassword(dataSourcePassword);

        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public void saveHotelFeatures() {
        List<HotelFeature> hotelFeatureList = jdbcTemplate.query(
                "select * from hotel_feature",
                new HotelFeatureMapper()
        );

        hotelFeatureRepository.save(hotelFeatureList);
    }

    public void extract(int limit) {
        List<Hotel> hotelList = jdbcTemplate.query(
                "select * from hotel h, property_type p, chain c " +
                "where h.property_type_id = p.id " +
                "and h.chain_id = c.id limit " + limit,
                new HotelMapper()
        );

        int i = 1;
        for (Hotel hotel : hotelList) {
            List<HotelFeature> hotelFeatureList = jdbcTemplate.query(
                    "select h.id, hf.type, hf.title from hotel h, hotel_feature_to_hotel hfh, hotel_feature hf " +
                            "where h.id = hfh.hotel_id " +
                            "and hfh.hotel_feature_id = hf.id " +
                            "and h.id = ?",
                    new Object[]{ hotel.id },
                    new HotelFeatureMapper()
            );

            convertAndSave(hotel, hotelFeatureList);

            System.out.println("i: " + i);
            i++;
        }
    }

    private void convertAndSave(Hotel hotel, List<HotelFeature> hotelFeatures) {
        HotelData hotelData = new HotelData();
        hotelData.setName(hotel.title);
        hotelData.setDescription(hotel.description);
        hotelData.setAddress(hotel.addressFull);
        hotelData.setLongitude(hotel.longitude);
        hotelData.setLatitude(hotel.latitude);
        hotelData.setEmail(hotel.email);
        hotelData.setEmail(hotel.email);
        hotelData.setWebsite(hotel.website);
        hotelData.setPhone(hotel.phone);
        hotelData.setFax(hotel.fax);
        hotelData.setStarRating(hotel.starRating);
        hotelData.setRooms(hotel.rooms);
        hotelData.setLowestPriceUSD(hotel.priceUSD);
        hotelData.setCheckIn(hotel.checkIn);
        hotelData.setCheckOut(hotel.checkOut);
        hotelData.setPropertyType(hotel.propertyType);
        hotelData.setChain(hotel.chain);

        if (hotel.ratingOverall != null) {
            HotelRatings hotelRatings = new HotelRatings();
            hotelRatings.setOverall(hotel.ratingOverall);
            hotelRatings.setOverallText(hotel.ratingOverallText);
            hotelRatings.setCleanliness(hotel.ratingCleanliness);
            hotelRatings.setDining(hotel.ratingDining);
            hotelRatings.setFacilities(hotel.ratingFacilities);
            hotelRatings.setLocation(hotel.ratingLocation);
            hotelRatings.setRooms(hotel.ratingRooms);
            hotelRatings.setService(hotel.ratingService);

            if (hotel.ratingPoints != null) {
                List<String> mainPoints = new ArrayList<>();
                String splitPoints[] = hotel.ratingPoints.split(";\\s?");
                for (String splitPoint : splitPoints) {
                    if (!splitPoint.isEmpty())
                        mainPoints.add(splitPoint);
                }
                if (!mainPoints.isEmpty())
                    hotelRatings.setMainPoints(mainPoints);
            }

            hotelData.setRatings(hotelRatings);
        }

        if (hotel.reviewsCount != null) {
            HotelReviews hotelReviews = new HotelReviews();
            hotelReviews.setCount(hotel.reviewsCount);

            if (hotel.reviewsSummaryPositive != null) {
                List<String> positive = new ArrayList<>();
                Matcher matcher = reviewsPattern.matcher(hotel.reviewsSummaryPositive);
                while (matcher.find()) {
                    positive.add(matcher.group(1));
                }
                if (!positive.isEmpty())
                    hotelReviews.setPositive(positive);
            }

            if (hotel.reviewsSummaryNegative != null) {
                List<String> negative = new ArrayList<>();
                Matcher matcher = reviewsPattern.matcher(hotel.reviewsSummaryNegative);
                while (matcher.find()) {
                    negative.add(matcher.group(1));
                }
                if (!negative.isEmpty())
                    hotelReviews.setNegative(negative);
            }

            hotelData.setReviews(hotelReviews);
        }

        if (!hotelFeatures.isEmpty()) {
            List<HotelFeature> toSave = new ArrayList<>();
            for (HotelFeature hotelFeature : hotelFeatures) {
                HotelFeature alreadyPresent = hotelFeatureRepository.findByName(hotelFeature.getName());
                if (alreadyPresent != null) // it should always be true
                    toSave.add(alreadyPresent);
            }

            hotelData.setFeatures(toSave);
        }

        hotelDataRepository.save(hotelData);
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

    private static class Hotel {

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

    private static class HotelMapper implements RowMapper<Hotel> {

        private DateTimeFormatter formatter = DateTimeFormat.forPattern("HH:mm a");
        private Pattern timePattern = Pattern.compile("\\d{1,2}:\\d{2}\\s?(AM|PM)");

        @Override
        public Hotel mapRow(ResultSet rs, int rowNum) throws SQLException {
            Hotel hotel = new Hotel();
            hotel.id = rs.getLong("h.id");
            hotel.title = rs.getString("h.title");
            hotel.addressFull = getNullIfEmpty(rs.getString("h.address_full"));
            String email = getNullIfEmpty(rs.getString("h.email"));
            String website = getNullIfEmpty(rs.getString("h.website"));
            String phone = getNullIfEmpty(rs.getString("h.phone"));
            String fax = getNullIfEmpty(rs.getString("h.fax"));
            hotel.longitude = getNullIfZero(rs.getDouble("h.longitude"));
            hotel.latitude = getNullIfZero(rs.getDouble("h.latitude"));
            hotel.starRating = getNullIfZero(rs.getFloat("h.star_rating"));
            hotel.rooms = getNullIfZero(rs.getInt("h.rooms"));
            hotel.priceUSD = getNullIfZero(rs.getInt("h.price_usd"));

            String checkIn = rs.getString("h.check_in");
            if (checkIn != null) {
                Matcher checkInMatcher = timePattern.matcher(checkIn);
                if (checkInMatcher.find()) {
                    hotel.checkIn = formatter.parseLocalTime(checkInMatcher.group());
                }
            }

            String checkOut = rs.getString("h.check_out");
            if (checkOut != null) {
                Matcher checkOutMatcher = timePattern.matcher(checkOut);
                if (checkOutMatcher.find()) {
                    hotel.checkOut = formatter.parseLocalTime(checkOutMatcher.group());
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

    private static class HotelFeatureMapper implements RowMapper<HotelFeature> {

        @Override
        public HotelFeature mapRow(ResultSet rs, int rowNum) throws SQLException {
            HotelFeature hotelFeature = new HotelFeature();
            hotelFeature.setName(rs.getString("title"));
            hotelFeature.setType(rs.getString("type"));
            return hotelFeature;
        }
    }
}
