package com.trainshier.repository;

import com.trainshier.entity.TrnRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface TrnRequestRepository extends JpaRepository<TrnRequest, Long> {
    List<TrnRequest> findByInstructorIdAndStatus(Long instructorId, String status);
    Optional<TrnRequest> findByStudentEmail(String studentEmail);
}
