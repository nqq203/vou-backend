package com.vou.user_service.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Entity
@Table(name = "session")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Session {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_session")
    private Long idSession;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "logout_at")
    private Timestamp logoutAt;

    @ManyToOne
    @JoinColumn(name = "id_user")
    private User user;
}
