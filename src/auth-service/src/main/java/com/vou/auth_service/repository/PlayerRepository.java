package com.vou.auth_service.repository;

import com.vou.auth_service.model.Admin;
import com.vou.auth_service.model.Player;
import com.vou.auth_service.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlayerRepository extends JpaRepository<Player, Long> {
    Player findByIdUser(Long idUser);
}
