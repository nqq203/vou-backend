package com.VOU.event_service.model;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "favouriteevent")
public class FavouriteEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_event", nullable = false)
    private Event event;

    @Column(name = "id_player", nullable = false)
    private Long idPlayer;

    @Column(name = "start_date", nullable = false)
    private Date startDate;

    @Column(name = "end_date", nullable = false)
    private Date endDate;

    public FavouriteEvent(Long id, Event event, Long idPlayer, Date startDate, Date endDate) {
        this.id = id;
        this.event = event;
        this.idPlayer = idPlayer;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public FavouriteEvent() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public Long getIdPlayer() {
        return idPlayer;
    }

    public void setIdPlayer(Long idPlayer) {
        this.idPlayer = idPlayer;
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
