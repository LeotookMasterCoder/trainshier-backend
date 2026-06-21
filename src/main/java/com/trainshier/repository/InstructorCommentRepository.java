package com.trainshier.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.trainshier.entity.InstructorComment;

@Repository
public interface InstructorCommentRepository
        extends JpaRepository<InstructorComment, Long> {

    List<InstructorComment>
    findByTransactionId(
            Long transactionId);
}