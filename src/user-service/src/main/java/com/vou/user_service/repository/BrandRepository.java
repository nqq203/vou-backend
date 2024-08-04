package com.vou.user_service.repository;

import com.vou.user_service.model.Brand;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BrandRepository extends JpaRepository<Brand, Long> {
    Brand findByIdUser(Long userId);
}
