package com.vou.event_service.repository;
import com.vou.event_service.model.BrandsCooperation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BrandsCooperationRepository extends JpaRepository<BrandsCooperation, Long> {
    BrandsCooperation findByIdBrandCooperation(Long idBrandCooperation);
    List<BrandsCooperation> findAllByIdEvent(Long idEvent);
    List<BrandsCooperation> findAllByIdBrand(Long idBrand);
    BrandsCooperation findByIdEvent(Long idEvent);
}