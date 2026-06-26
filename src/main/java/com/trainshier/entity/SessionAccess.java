package com.trainshier.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "session_access")
@Data
public class SessionAccess {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "login_at")
    private LocalDateTime loginAt;

    @Column(name = "logout_at")
    private LocalDateTime logoutAt;

    @ManyToOne
    @JoinColumn(name = "session_id", nullable = false)
    private SimulationSession session;

    @ManyToOne
    @JoinColumn(name = "apprentice_id", nullable = false)
    private User apprentice;
}