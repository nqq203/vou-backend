package com.vou.event_service.repository;
import com.vou.event_service.model.BrandsCooperation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BrandsCooperationRepository extends JpaRepository<BrandsCooperation, Long> {
    BrandsCooperation findByIdBrandCooperation(Long idBrandCooperation);
}