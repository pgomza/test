package com.horeca.site.services;

import com.horeca.site.models.hotel.address.Address;
import com.horeca.site.models.hotel.address.AddressTranslation;
import com.horeca.site.models.hotel.address.AddressView;
import org.springframework.stereotype.Service;

@Service
public class AddressService {

    public AddressView getView(Address address, String preferredLanguage, String defaultLanguage) {
        AddressTranslation translation = address.getTranslation(preferredLanguage, defaultLanguage);

        AddressView view = new AddressView();
        view.setCity(translation.getCity());
        view.setStreet(translation.getStreet());

        return view;
    }
}
