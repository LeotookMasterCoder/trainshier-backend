package com.trainshier.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.trainshier.entity.InstructorComment;
import com.trainshier.repository.InstructorCommentRepository;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class InstructorCommentController {

    private final InstructorCommentRepository repository;

    @GetMapping
    public List<InstructorComment> findAll() {
        return repository.findAll();
    }

    @PostMapping
    public InstructorComment save(
            @RequestBody InstructorComment comment) {
        if (comment.getDate() == null) {
            comment.setDate(LocalDateTime.now());
        }
        return repository.save(comment);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        repository.deleteById(id);
    }
}
