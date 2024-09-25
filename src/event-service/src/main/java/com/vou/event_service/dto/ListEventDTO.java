package com.vou.event_service.dto;

import com.vou.event_service.model.Event;
import lombok.Data;

import java.sql.Timestamp;
import java.time.Instant;


@Data
public class ListEventDTO {
    private Long idEvent;
    private String eventName;
    private String imageUrl;
    private int numberOfVouchers;
    private Timestamp startDate;
    private Timestamp endDate;
    private String status;
    String brandLogo;

    public ListEventDTO(Event event, String brandLogo) {
        this.idEvent = event.getIdEvent();
        this.eventName = event.getEventName();
        this.imageUrl = event.getImageUrl();
        this.numberOfVouchers = event.getNumberOfVouchers();
        this.startDate = event.getStartDate();
        this.endDate = event.getEndDate();
        this.status = determineStatus();
        this.brandLogo = brandLogo;
    }
    private String determineStatus() {
        Timestamp now = Timestamp.from(Instant.now());

        if (now.before(startDate)) {
            return "pending";
        } else if (now.after(endDate)) {
            return "inactive";
        } else {
            return "active";
        }
    }
}
