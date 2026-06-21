package com.trainshier.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "comentarios_instructor")
@Data
public class InstructorComment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "comentario", nullable = false)
    private String comment;

    @Column(name = "calificacion")
    private Double score;

    @Column(name = "fecha")
    private LocalDateTime date;

    @ManyToOne
    @JoinColumn(name = "transaccion_id")
    private Transaction transaction;

    @ManyToOne
    @JoinColumn(name = "instructor_id")
    private User instructor;
}