package com.vou.user_service.repository;

import com.vou.user_service.constant.Status;
import com.vou.user_service.model.Brand;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BrandRepository extends JpaRepository<Brand, Long> {
    Brand findByIdUser(Long userId);

    List<Brand> findBrandByStatusIs(Status status);
}
