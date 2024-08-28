package com.VOU.event_service.model;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "event")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_event")
    private Long idEvent;

    @Column(name = "event_name", nullable = false)
    private String eventName;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "number_of_vouchers")
    private Integer numberOfVouchers;

    @Column(name = "start_date", nullable = false)
    private Date startDate;

    @Column(name = "end_date", nullable = false)
    private Date endDate;

    public Event() {
    }

    public Event(Long idEvent, String eventName, String imageUrl, Integer numberOfVouchers, Date startDate, Date endDate) {
        this.idEvent = idEvent;
        this.eventName = eventName;
        this.imageUrl = imageUrl;
        this.numberOfVouchers = numberOfVouchers;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Long getIdEvent() {
        return idEvent;
    }

    public void setIdEvent(Long idEvent) {
        this.idEvent = idEvent;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Integer getNumberOfVouchers() {
        return numberOfVouchers;
    }

    public void setNumberOfVouchers(Integer numberOfVouchers) {
        this.numberOfVouchers = numberOfVouchers;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
}
