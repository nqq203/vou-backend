package com.VOU.event_service.model;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "favouriteevent")
public class FavouriteEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_event_id_player")
    private Long idFavouriteEvent;

    @Column(name = "id_player", nullable = false)
    private Long idPlayer;

    @Column(name = "start_date", nullable = false)
    private Date startDate;

    @Column(name = "end_date", nullable = false)
    private Date endDate;
}
