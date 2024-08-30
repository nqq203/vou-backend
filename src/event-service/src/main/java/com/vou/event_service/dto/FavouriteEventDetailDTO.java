package com.vou.event_service.dto;

import com.vou.event_service.model.Event;
import com.vou.event_service.model.FavouriteEvent;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class FavouriteEventDetailDTO {
    Long idFavouriteEvent;
    Long idEvent;
    String eventBanner;
    String eventName;
    Date startDate;
    Date endDate;
    String brandLogo;
    Long idBrand;

    public FavouriteEventDetailDTO(FavouriteEvent favouriteEvent, Event event, String brandLogo, Long idBrand) {
        this.idFavouriteEvent = favouriteEvent.getIdFavouriteEvent();
        this.idEvent = favouriteEvent.getIdEvent();
        this.eventBanner = event.getImageUrl();
        this.eventName = event.getEventName();
        this.startDate = favouriteEvent.getStartDate();
        this.endDate = favouriteEvent.getEndDate();
        this.brandLogo = brandLogo;
    }
}
