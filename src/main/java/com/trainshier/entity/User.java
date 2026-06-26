package com.trainshier.entity;

import com.trainshier.enums.UserRole;
import com.trainshier.converter.UserRoleConverter;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/** System user entity. */
@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "role", nullable = false)
    @Convert(converter = UserRoleConverter.class)
    private UserRole role;

    @Column(name = "registration_date", nullable = false,
            insertable = false, updatable = false)
    private java.time.LocalDate registrationDate;

    @Column(name = "rfid_uid", unique = true)
    private String rfidUid;

    @Column(name = "active", nullable = false)
    private Boolean active = true;
}