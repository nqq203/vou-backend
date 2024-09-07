package com.vou.statistics_service.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class User {
    private Long id;
    private String fullName;
    private String email;
    private String imageUrl;
}
