package com.VOU.event_service.repository;
import com.VOU.event_service.model.BrandsCoperation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BrandsCoperationRepository extends JpaRepository<BrandsCoperation, Long> {
}