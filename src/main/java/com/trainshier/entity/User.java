package com.trainshier.entity;

import com.trainshier.enums.UserRole;
import com.trainshier.converter.UserRoleConverter;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * System user entity.
 */
@Entity
@Table(name = "usuarios")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Long id;

    @Column(name = "nombre", nullable = false)
    private String name;

    @Column(name = "correo", unique = true, nullable = false)
    private String email;

    @Column(name = "contraseña", nullable = false)
    private String password;

    @Column(name = "rol", nullable = false)
    @Convert(converter = UserRoleConverter.class)
    private UserRole role;

    @Column(name = "fecha_registro", nullable = false, insertable = false, updatable = false)
    private java.time.LocalDate registrationDate;

    @Column(name = "rfid_uid", unique = true)
    private String rfidUid;
}