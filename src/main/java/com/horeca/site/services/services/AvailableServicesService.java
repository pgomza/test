package com.horeca.site.services.services;

import com.horeca.site.models.Currency;
import com.horeca.site.models.Price;
import com.horeca.site.models.hotel.Hotel;
import com.horeca.site.models.hotel.services.AvailableServices;
import com.horeca.site.models.hotel.services.bar.Bar;
import com.horeca.site.models.hotel.services.breakfast.Breakfast;
import com.horeca.site.models.hotel.services.carpark.CarPark;
import com.horeca.site.models.hotel.services.hairdresser.HairDresser;
import com.horeca.site.models.hotel.services.housekeeping.Housekeeping;
import com.horeca.site.models.hotel.services.petcare.PetCare;
import com.horeca.site.models.hotel.services.rental.Rental;
import com.horeca.site.models.hotel.services.rental.RentalCategory;
import com.horeca.site.models.hotel.services.restaurantmenu.RestaurantMenu;
import com.horeca.site.models.hotel.services.roomservice.RoomService;
import com.horeca.site.models.hotel.services.roomservice.RoomServiceCategory;
import com.horeca.site.models.hotel.services.spa.Spa;
import com.horeca.site.models.hotel.services.spacall.SpaCall;
import com.horeca.site.models.hotel.services.tableordering.TableOrdering;
import com.horeca.site.models.hotel.services.taxi.Taxi;
import com.horeca.site.repositories.services.AvailableServicesRepository;
import com.horeca.site.services.HotelService;
import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Transactional
public class AvailableServicesService {

    private static final DateTimeFormatter localTimeFormatter = DateTimeFormat.forPattern("HH:mm");

    @Autowired
    private HotelService hotelService;

    @Autowired
    private AvailableServicesRepository repository;

    public AvailableServices get(Long hotelId) {
        Hotel hotel = hotelService.get(hotelId);
        return hotel.getAvailableServices();
    }

    public AvailableServices update(AvailableServices services) {
        return repository.save(services);
    }

    public void ensureFullyInitialized(AvailableServices services) {
        if (services.getBreakfast() == null) {
            services.setBreakfast(getDefaultBreakfast());
        }
        if (services.getCarPark() == null) {
            services.setCarPark(getDefaultCarPark());
        }
        if (services.getHousekeeping() == null) {
            services.setHousekeeping(getDefaultHousekeeping());
        }
        if (services.getSpa() == null) {
            services.setSpa(getDefaultSpa());
        }
        if (services.getPetCare() == null) {
            services.setPetCare(getDefaultPetCare());
        }
        if (services.getTaxi() == null) {
            services.setTaxi(getDefaultTaxi());
        }
        if (services.getRoomService() == null) {
            services.setRoomService(getDefaultRoomService());
        }
        if (services.getTableOrdering() == null) {
            services.setTableOrdering(getDefaultTableOrdering());
        }
        if (services.getBar() == null) {
            services.setBar(getDefaultBar());
        }
        if (services.getSpaCall() == null) {
            services.setSpaCall(getDefaultSpaCall());
        }
        if (services.getHairDresser() == null) {
            services.setHairDresser(getDefaultHairDresser());
        }
        if (services.getRental() == null) {
            services.setRental(getDefaultRental());
        }
        if (services.getRestaurantMenu() == null) {
            services.setRestaurantMenu(getDefaultRestaurantMenu());
        }
    }

    private Breakfast getDefaultBreakfast() {
        Price price = new Price();
        price.setCurrency(Currency.EUR);
        price.setValue(new BigDecimal(5));
        LocalTime fromHour = localTimeFormatter.parseLocalTime("08:00");
        LocalTime toHour = localTimeFormatter.parseLocalTime("11:00");

        return new Breakfast("", new ArrayList<>(), false, price, fromHour, toHour);
    }

    private CarPark getDefaultCarPark() {
        CarPark carPark = new CarPark();
        carPark.setDescription("");
        Price carParkPrice = new Price();
        carParkPrice.setCurrency(Currency.EUR);
        carParkPrice.setValue(new BigDecimal(5));
        carPark.setPrice(carParkPrice);
        carPark.setAvailable(false);
        return carPark;
    }

    private Housekeeping getDefaultHousekeeping() {
        Housekeeping housekeeping = new Housekeeping();
        housekeeping.setDescription("");
        Price housekeepingPrice = new Price();
        housekeepingPrice.setCurrency(Currency.EUR);
        housekeepingPrice.setValue(new BigDecimal(5));
        housekeeping.setPrice(housekeepingPrice);
        housekeeping.setAvailable(false);
        return housekeeping;
    }

    private Spa getDefaultSpa() {
        Spa spa = new Spa();
        spa.setDescription("");
        Price spaPrice = new Price();
        spaPrice.setCurrency(Currency.EUR);
        spaPrice.setValue(new BigDecimal(5));
        spa.setPrice(spaPrice);
        spa.setAvailable(false);
        return spa;
    }

    private PetCare getDefaultPetCare() {
        PetCare petCare = new PetCare();
        petCare.setDescription("");
        Price petCarePrice = new Price();
        petCarePrice.setCurrency(Currency.EUR);
        petCarePrice.setValue(new BigDecimal(5));
        petCare.setPrice(petCarePrice);
        petCare.setAvailable(false);
        return petCare;
    }

    private Taxi getDefaultTaxi() {
        Taxi taxi = new Taxi();
        Price taxiPrice = new Price();
        taxiPrice.setCurrency(Currency.EUR);
        taxiPrice.setValue(new BigDecimal(5));
        taxi.setPrice(taxiPrice);
        taxi.setAvailable(false);
        return taxi;
    }

    private RoomService getDefaultRoomService() {
        RoomService roomService = new RoomService();
        roomService.setDescription("");
        Price carParkPrice = new Price();
        carParkPrice.setCurrency(Currency.EUR);
        carParkPrice.setValue(new BigDecimal(5));
        roomService.setPrice(carParkPrice);

        RoomServiceCategory snackCategory = new RoomServiceCategory();
        snackCategory.setCategory(RoomServiceCategory.Category.SNACK);
        RoomServiceCategory drinkCategory = new RoomServiceCategory();
        drinkCategory.setCategory(RoomServiceCategory.Category.DRINK);
        Set<RoomServiceCategory> categories = new HashSet<>();
        categories.add(snackCategory);
        categories.add(drinkCategory);
        roomService.setCategories(categories);

        roomService.setAvailable(false);

        return roomService;
    }

    private TableOrdering getDefaultTableOrdering() {
        TableOrdering tableOrdering = new TableOrdering("");
        tableOrdering.setAvailable(false);
        return tableOrdering;
    }

    private Bar getDefaultBar() {
        Price price = new Price();
        price.setCurrency(Currency.EUR);
        price.setValue(new BigDecimal(5));
        LocalTime fromHour = localTimeFormatter.parseLocalTime("08:00");
        LocalTime toHour = localTimeFormatter.parseLocalTime("11:00");

        return new Bar("", new ArrayList<>(), false, price, fromHour, toHour);
    }

    private SpaCall getDefaultSpaCall() {
        SpaCall spaCall = new SpaCall();
        spaCall.setDescription("");
        spaCall.setNumber("+12 345 678 901");
        spaCall.setAvailable(false);
        return spaCall;
    }

    private HairDresser getDefaultHairDresser() {
        HairDresser hairDresser = new HairDresser();
        hairDresser.setDescription("");
        hairDresser.setNumber("+12 345 678 901");
        hairDresser.setAvailable(false);
        return hairDresser;
    }

    private RestaurantMenu getDefaultRestaurantMenu() {
        RestaurantMenu menu = new RestaurantMenu();
        menu.setDescription("");
        menu.setAvailable(false);
        return menu;
    }

    private Rental getDefaultRental() {
        Rental rental = new Rental();
        rental.setDescription("");
        Price price = new Price();
        price.setCurrency(Currency.EUR);
        price.setValue(new BigDecimal(5));
        rental.setPrice(price);
        rental.setFromHour(localTimeFormatter.parseLocalTime("08:00"));
        rental.setToHour(localTimeFormatter.parseLocalTime("11:00"));

        Set<RentalCategory> rentalCategories = Stream.of(RentalCategory.Category.values())
                .map(categoryName -> {
                    RentalCategory category = new RentalCategory();
                    category.setCategory(categoryName);
                    return category;
                })
                .collect(Collectors.toSet());

        rental.setCategories(rentalCategories);

        rental.setAvailable(false);

        return rental;
    }
}
