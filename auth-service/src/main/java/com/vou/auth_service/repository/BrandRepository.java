package com.vou.auth_service.repository;

import com.vou.auth_service.model.Brand;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BrandRepository extends JpaRepository<Brand, Long> {
    Brand findByIdUser(Long userId);
}
