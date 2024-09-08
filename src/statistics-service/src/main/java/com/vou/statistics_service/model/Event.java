package com.vou.statistics_service.model;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Setter
@Getter
public class Event {
        private Long idEvent;

        private String eventName;

        private String imageUrl;

        private Long shareCount;

        private Integer numberOfVouchers;

        private Integer remainingVouchers;

        private Timestamp startDate;

        private Timestamp endDate;

        private Timestamp deletedDate;

        private Long createdBy;
}
