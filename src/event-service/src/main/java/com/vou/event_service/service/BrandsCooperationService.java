package com.vou.event_service.service;

import com.vou.event_service.common.BadRequestException;
import com.vou.event_service.common.NotFoundException;
import com.vou.event_service.entity.CreateBrandsCooperationRequest;
import com.vou.event_service.model.BrandsCooperation;
import com.vou.event_service.repository.BrandsCooperationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.NotActiveException;
import java.util.List;
import java.util.Optional;

@Service
public class BrandsCooperationService {
    @Autowired
    private BrandsCooperationRepository brandsCooperationRepository;

    public List<BrandsCooperation> getAllBrandsCooperation() throws Exception {
        try {
            return brandsCooperationRepository.findAll();
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    public BrandsCooperation getBrandsCooperationById(Long id) throws Exception {
        try {
            BrandsCooperation result = brandsCooperationRepository.findByIdBrandCooperation(id);
            if (result == null) {
                throw new NotActiveException("Brand cooperation not found");
            } else {
                return result;
            }
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    public boolean deleteBrandsCooperationById(Long id) throws Exception {
        try {
            BrandsCooperation result = brandsCooperationRepository.findByIdBrandCooperation(id);
            if (result == null) {
                throw new NotActiveException("Brand cooperation not found");
            } else {
                brandsCooperationRepository.delete(result);
                return true;
            }
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    public BrandsCooperation updateBrandsCooperation(Long id, CreateBrandsCooperationRequest request) throws Exception {
        try {
            BrandsCooperation newBrandsCooperation = brandsCooperationRepository.findByIdBrandCooperation(id);
            if (newBrandsCooperation == null) {
                throw new NotFoundException("Brand cooperation not found");
            }
            Optional.ofNullable(request.getIdBrand()).ifPresent(action ->
                    newBrandsCooperation.setIdBrandCooperation(request.getIdBrand()));
            Optional.ofNullable(request.getIdEvent()).ifPresent(action ->
                    newBrandsCooperation.setIdEvent(request.getIdEvent()));

            brandsCooperationRepository.save(newBrandsCooperation);
            return newBrandsCooperation;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    public BrandsCooperation createBrandsCooperation(CreateBrandsCooperationRequest request) throws Exception {
        BrandsCooperation newBrandsCooperation = new BrandsCooperation();
        if (request.getIdBrand() == null || request.getIdEvent() == null) {
            throw new BadRequestException("Invalid input");
        }
        newBrandsCooperation.setIdBrand(request.getIdBrand());
        newBrandsCooperation.setIdEvent(request.getIdEvent());
        try {
            brandsCooperationRepository.save(newBrandsCooperation);
            return newBrandsCooperation;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }
}
