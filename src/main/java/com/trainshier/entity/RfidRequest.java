package com.trainshier.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.Data;

/** RFID card request submitted by an apprentice. */
@Entity
@Table(name = "rfid_requests")
@Data
public class RfidRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "rfid_uid", nullable = false)
    private String rfidUid;

    /** pending | approved | rejected */
    @Column(name = "status", nullable = false)
    private String status = "pending";

    @Column(name = "requested_at")
    private LocalDateTime requestedAt;

    @Column(name = "reviewed_at")
    private LocalDateTime reviewedAt;

    @ManyToOne
    @JoinColumn(name = "reviewed_by")
    private User reviewedBy;

    @Column(name = "notes")
    private String notes;
}
