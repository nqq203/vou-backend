package com.vou.user_service.repository;

import com.vou.user_service.model.Admin;
import com.vou.user_service.model.Player;
import com.vou.user_service.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlayerRepository extends JpaRepository<Player, Long> {
    Player findByIdUser(Long idUser);
}
