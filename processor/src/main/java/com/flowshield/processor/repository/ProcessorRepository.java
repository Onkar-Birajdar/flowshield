package com.flowshield.processor.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.flowshield.processor.model.Job;

@Repository
public interface ProcessorRepository extends JpaRepository<Job, String> {
  @Query(value = """
      SELECT * FROM jobs
      WHERE status = 'CREATED'
      AND (locked_until IS NULL OR locked_until < now())
      ORDER BY created_at
      LIMIT 1
      FOR UPDATE SKIP LOCKED
      """, nativeQuery = true)
  Optional<Job> findNextJobForUpdate();

  Optional<Job> findByJobId(String jobId);
}