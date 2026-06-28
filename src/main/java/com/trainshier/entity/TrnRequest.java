package com.trainshier.entity;

import java.time.LocalDateTime;
import jakarta.persistence.*;
import lombok.Data;

/** TRN registration code authorization request. */
@Entity
@Table(name = "trn_requests")
@Data
public class TrnRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "instructor_id", nullable = false)
    private User instructor;

    @Column(name = "student_name", nullable = false)
    private String studentName;

    @Column(name = "student_email", nullable = false, unique = true)
    private String studentEmail;

    @Column(name = "trn_code")
    private String trnCode;

    /** PENDING | APPROVED | REJECTED */
    @Column(name = "status", nullable = false)
    private String status = "PENDING";

    @Column(name = "requested_at")
    private LocalDateTime requestedAt = LocalDateTime.now();

    @Column(name = "reviewed_at")
    private LocalDateTime reviewedAt;
}
