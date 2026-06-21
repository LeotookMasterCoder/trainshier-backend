package com.trainshier.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.trainshier.entity.SessionAccess;

@Repository
public interface SessionAccessRepository
        extends JpaRepository<SessionAccess, Long> {

    List<SessionAccess> findByApprentice_Id(Long apprenticeId);

    List<SessionAccess> findBySession_Id(Long sessionId);
}