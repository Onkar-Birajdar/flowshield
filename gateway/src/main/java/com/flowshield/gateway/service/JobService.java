package com.flowshield.gateway.service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.flowshield.common.dto.JobDetailResponse;
import com.flowshield.common.dto.JobRequest;
import com.flowshield.common.dto.CreateJobResponse;
import com.flowshield.common.dto.JobStatus;
import com.flowshield.gateway.exception.JobNotFoundException;
import com.flowshield.gateway.mapper.JobMapper;
import com.flowshield.gateway.model.Job;
import com.flowshield.gateway.repository.JobRepository;

@Service
public class JobService {

    private final JobRepository jobRepository;
    private final JobMapper jobMapper;

    public JobService(JobRepository jobRepository, JobMapper jobMapper) {
        this.jobRepository = jobRepository;
        this.jobMapper = jobMapper;
    }

    @Transactional
    public CreateJobResponse createJob(String idempotencyKey, JobRequest request) {

        Optional<Job> existing = jobRepository.findByIdempotencyKey(idempotencyKey);

        if (existing.isPresent()) {
            return jobMapper.toCreateResponse(existing.get());
        }

        Job job = Job.builder()
                .jobId(UUID.randomUUID().toString())
                .jobType(request.getJobType())
                .payload(request.getPayload())
                .status(JobStatus.CREATED)
                .idempotencyKey(idempotencyKey)
                .createdAt(LocalDateTime.now())
                .build();

        jobRepository.save(job);

        return jobMapper.toCreateResponse(job);
    }

    public JobDetailResponse getJob(String jobId) {

        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new JobNotFoundException("Job not found"));

        return jobMapper.toDetailResponse(job);
    }

}
