package com.vou.auth_service.repository;

import com.vou.auth_service.model.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepository extends JpaRepository<Admin, Long> {
    Admin findByIdUser(Long idUser);
}
