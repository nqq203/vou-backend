package com.vou.event_service.service;

import com.vou.event_service.dto.FavouriteEventDetailDTO;
import com.vou.event_service.model.BrandsCooperation;
import com.vou.event_service.model.Event;
import com.vou.event_service.model.FavouriteEvent;
import com.vou.event_service.repository.BrandsCooperationRepository;
import com.vou.event_service.repository.EventRepository;
import com.vou.event_service.repository.FavouriteEventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class FavouriteEventService {
    @Autowired
    private FavouriteEventRepository favouriteEventRepository;
    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private BrandsCooperationRepository brandsCooperationRepository;
    @Autowired
    private BrandClient brandClient;

    public FavouriteEvent addFavouriteEvent(Long idEvent, Long idPlayer, String username, Event event) {
        try {
            FavouriteEvent favouriteEvent = new FavouriteEvent();
            favouriteEvent.setIdEvent(idEvent);
            favouriteEvent.setIdPlayer(idPlayer);
            favouriteEvent.setUsername(username);
            favouriteEvent.setStartDate(event.getStartDate());
            favouriteEvent.setEndDate(event.getEndDate());
            favouriteEvent.setDeletedDate(null);
            return favouriteEventRepository.save(favouriteEvent);
        } catch (Exception e) {
            return null;
        }
    }

    public List<FavouriteEvent> findByIdPlayer(Long idPlayer) {
        try {
            Date now = new Date();
            return favouriteEventRepository.findAllByIdPlayerAndEndDateAfterAndDeletedDateIsNull(idPlayer, now);
        } catch (Exception e) {
            return null;
        }
    }

    public List<FavouriteEventDetailDTO> getAllFavouriteEvent(List<FavouriteEvent> favouriteEventList) {
        try {
            List<FavouriteEventDetailDTO> list = new ArrayList<>();
            favouriteEventList.forEach(fEvent -> {
                Date now = new Date();
                Event e = eventRepository.findByIdEventAndEndDateAfterAndDeletedDateIsNull(fEvent.getIdEvent(), now);
                String brandLogo = brandClient.getBrandLogo(e.getCreatedBy()).orElse(null);
                FavouriteEventDetailDTO favouriteEventDetailDTO = new FavouriteEventDetailDTO(fEvent, e, brandLogo, e.getCreatedBy());
                list.add(favouriteEventDetailDTO);
            });
            return list;
        } catch (Exception e) {
            return null;
        }
    }

    public FavouriteEvent findByIdEventAndIdPlayer(Long idEvent, Long idPlayer) {
        try {
            return favouriteEventRepository.findByIdEventAndIdPlayer(idEvent, idPlayer);
        } catch (Exception e) {
            return null;
        }
    }

    public boolean deleteFavouriteEvent(Long idFavouriteEvent) {
        try {
            favouriteEventRepository.deleteById(idFavouriteEvent);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
