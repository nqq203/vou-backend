package com.vou.event_service.repository;
import com.vou.event_service.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findByEventNameContainingIgnoreCase(String eventName);
    Event findByIdEvent(Long idEvent);

    @Query("SELECT e FROM Event e WHERE e.startDate <= :currentTimestamp AND e.endDate >= :currentTimestamp")
    List<Event> findActiveEvents(@Param("currentTimestamp") Timestamp currentTimestamp);
}