package com.horeca.site.services;

import com.horeca.site.exceptions.ResourceNotFoundException;
import com.horeca.site.models.hotel.Hotel;
import com.horeca.site.models.hotel.Link;
import com.horeca.site.repositories.LinkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class LinkService {

    @Autowired
    private LinkRepository repository;

    @Autowired
    private HotelService hotelService;

    public List<Link> getAll(Long hotelId) {
        Hotel hotel = hotelService.get(hotelId);
        return hotel.getLinks();
    }

    public Link get(Long hotelId, Long id) {
        List<Link> links = getAll(hotelId);
        Optional<Link> linkOpt = links.stream().filter(l -> id.equals(l.getId())).findAny();
        if (linkOpt.isPresent()) {
            return linkOpt.get();
        } else {
            throw new ResourceNotFoundException("Could not find a link with such an id");
        }
    }

    public Link save(Long hotelId, Link entity) {
        if (entity.getId() != null) {
            Link existingLink = get(hotelId, entity.getId());
            existingLink.setName(entity.getName());
            existingLink.setValue(entity.getValue());
            repository.save(existingLink);
            return existingLink;
        }
        else {
            Hotel hotel = hotelService.get(hotelId);
            Link newLink = repository.save(entity);
            hotel.getLinks().add(newLink);
            hotelService.update(hotelId, hotel);
            return newLink;
        }
    }

    public List<Link> save(Long hotelId, List<Link> links) {
        Hotel hotel = hotelService.get(hotelId);
        hotel.getLinks().clear();
        hotel.getLinks().addAll(links);
        hotelService.update(hotelId, hotel);
        return hotel.getLinks();
    }

    public void delete(Long hotelId, Long id) {
        get(hotelId, id);
        Hotel hotel = hotelService.get(hotelId);
        hotel.getLinks().removeIf(link -> link.getId().equals(id));
        hotelService.update(hotelId, hotel);
    }
}