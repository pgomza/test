package com.horeca.site.services;

import com.horeca.site.exceptions.ResourceNotFoundException;
import com.horeca.site.models.hoteldata.HotelData;
import com.horeca.site.models.hoteldata.HotelDataView;
import com.horeca.site.repositories.hoteldata.HotelDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@Transactional
public class HotelDataService {

    @Autowired
    private HotelDataRepository repository;

    public HotelDataView get(Long id) {
        HotelData hotelData = repository.findOne(id);
        if (hotelData == null)
            throw new ResourceNotFoundException();

        return hotelData.toView();
    }

    public HotelDataView getByName(String name) {
        // TODO implement the actual functionality; for now return a random Hotel from the first 100
        Page<HotelData> firstPage = repository.findAll(new PageRequest(0, 100));
        List<HotelData> batch = new ArrayList<>(firstPage.getContent());
        Collections.shuffle(batch);
        return batch.get(0).toView();
    }

    public Page<HotelDataView> getByCity(String city) {
        return null;
    }

    public Page<HotelDataView> getBatch(Pageable pageable) {
        Iterable<HotelData> batch = repository.findAll(pageable);
        List<HotelDataView> views = new ArrayList<>();
        for (HotelData hotelData : batch) {
            views.add(hotelData.toView());
        }

        PageImpl<HotelDataView> result = new PageImpl<HotelDataView>(views, pageable, repository.getTotalCount());
        return result;
    }
}
