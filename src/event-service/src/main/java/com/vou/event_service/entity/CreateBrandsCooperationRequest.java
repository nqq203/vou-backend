package com.vou.event_service.entity;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateBrandsCooperationRequest {
    private Long idEvent;

    private Long idBrand;

    public CreateBrandsCooperationRequest(Long idEvent, Long idBrand) {
        this.idEvent = idEvent;
        this.idBrand = idBrand;
    }
}
