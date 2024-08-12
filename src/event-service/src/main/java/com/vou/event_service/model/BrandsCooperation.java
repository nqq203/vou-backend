package com.vou.event_service.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "brandscooperation")
@Getter
@Setter
public class BrandsCooperation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_event_id_brand")
    private Long idBrandCooperation;

    @Column(name = "id_event")
    private Long idEvent;

    @Column(name = "id_brand")
    private Long idBrand;
}
