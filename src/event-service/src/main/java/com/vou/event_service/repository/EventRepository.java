package com.vou.event_service.repository;
import com.vou.event_service.model.Event;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findByEventNameContainingIgnoreCase(String eventName);
    Event findByIdEvent(Long idEvent);

    @Query("SELECT e FROM Event e WHERE e.startDate <= :currentTimestamp AND e.endDate >= :currentTimestamp AND e.deletedDate IS NULL")
    List<Event> findActiveEvents(@Param("currentTimestamp") Timestamp currentTimestamp);

    Event findByIdEventAndEndDateAfterAndDeletedDateIsNull(Long idEvent, Date currentDate);


    @Query("SELECT e FROM Event e WHERE e.endDate BETWEEN :currentDate AND :threeDaysAfter")
    List<Event> findEventsStartingInThreeDays(@Param("currentDate") Timestamp currentDate,
                                              @Param("threeDaysAfter") Timestamp threeDaysAfter);

    @Modifying
    @Transactional
    @Query("UPDATE Event e SET e.remainingVouchers = e.remainingVouchers - 1 WHERE e.idEvent = :idEvent")
    int decreaseEventRemainingVoucherByIdEvent(@Param("idEvent") Long idEvent);

    @Query("SELECT e FROM Event e JOIN FavouriteEvent f ON e.idEvent = f.idEvent WHERE e.endDate BETWEEN :currentDate AND :threeDaysAfter AND f.username = :username")
    List<Event> findEventStartingInThreeDaysUserName(
            @Param("username") String username,
            @Param("currentDate") Timestamp currentDate,
            @Param("threeDaysAfter") Timestamp threeDaysAfter);


}