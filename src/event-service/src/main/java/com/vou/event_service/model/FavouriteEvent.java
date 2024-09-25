package com.vou.event_service.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Entity
@Table(name = "favouriteevent")
@Getter
@Setter
public class FavouriteEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_event_id_player")
    private Long idFavouriteEvent;

    @Column(name = "id_player", nullable = false)
    private Long idPlayer;

    @Column(name= "username")
    private String username;

    @Column(name = "id_event", nullable = false)
    private Long idEvent;

    @Column(name = "start_date", nullable = false)
    private Date startDate;

    @Column(name = "end_date", nullable = false)
    private Date endDate;

    @Column(name = "deleted_date")
    private Date deletedDate;
}
