package com.vou.user_service.repository;

import com.vou.user_service.model.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepository extends JpaRepository<Admin, Long> {
    Admin findByIdUser(Long idUser);
}
