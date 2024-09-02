package com.vou.event_service.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.sql.Timestamp;

@Getter
@Setter
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
    private Timestamp startDate;

    @Column(name = "end_date", nullable = false)
    private Timestamp endDate;

    @Column(name = "deleted_date")
    private Timestamp deletedDate;

    @Column(name = "created_by")
    private Long createdBy;
}
