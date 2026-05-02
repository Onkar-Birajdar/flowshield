package com.flowshield.gateway.repository;

import com.flowshield.common.dto.JobStatus;
import com.flowshield.gateway.model.Job;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface JobRepository extends JpaRepository<Job, String> {
    Optional<Job> findByIdempotencyKey(String key);
    List<Job> findByStatus(JobStatus status);
}

