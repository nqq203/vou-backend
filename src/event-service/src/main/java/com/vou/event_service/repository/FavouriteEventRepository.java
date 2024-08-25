package com.vou.event_service.repository;

import com.vou.event_service.model.FavouriteEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FavouriteEventRepository extends JpaRepository<FavouriteEvent, Long> {
}