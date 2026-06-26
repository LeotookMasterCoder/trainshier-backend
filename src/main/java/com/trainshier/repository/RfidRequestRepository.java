package com.trainshier.repository;

import com.trainshier.entity.RfidRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RfidRequestRepository extends JpaRepository<RfidRequest, Long> {
    List<RfidRequest> findByStatus(String status);
    List<RfidRequest> findByUserId(Long userId);
}
