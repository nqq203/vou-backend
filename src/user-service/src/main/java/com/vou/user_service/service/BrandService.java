package com.vou.user_service.service;

import com.vou.user_service.constant.Status;
import com.vou.user_service.model.Brand;
import com.vou.user_service.repository.BrandRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BrandService {
    @Autowired
    private BrandRepository brandRepository;

    public List<Brand> getAllActiveBrands() {
        return brandRepository.findBrandByStatusIs(Status.ACTIVE);
    }
}
