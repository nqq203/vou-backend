package com.vou.event_service.repository;

import com.vou.event_service.model.BrandsCooperation;
import com.vou.event_service.model.FavouriteEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface FavouriteEventRepository extends JpaRepository<FavouriteEvent, Long> {
    List<FavouriteEvent> findAllByIdPlayerAndEndDateAfterAndDeletedDateIsNull(Long idPlayer, Date currentDate);
    List<FavouriteEvent> findAllByIdEvent(Long idEvent);
//    FavouriteEvent findByIdEvent(Long idEvent);
    FavouriteEvent findByIdEventAndIdPlayer(Long idEvent, Long idPlayer);

}