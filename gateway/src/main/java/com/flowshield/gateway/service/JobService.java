package com.flowshield.gateway.service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import com.flowshield.common.dto.JobDetailResponse;
import com.flowshield.common.dto.JobRequest;
import com.flowshield.common.dto.CreateJobResponse;
import com.flowshield.common.dto.JobStatus;
import com.flowshield.gateway.exception.JobNotFoundException;
import com.flowshield.gateway.mapper.JobMapper;
import com.flowshield.gateway.model.Job;
import com.flowshield.gateway.repository.JobRepository;
import org.springframework.stereotype.Service;



@Service
public class JobService {

    private final JobRepository jobRepository;
    private final JobMapper jobMapper;
    public JobService(JobRepository jobRepository, JobMapper jobMapper) {
        this.jobRepository = jobRepository;
        this.jobMapper = jobMapper;
    }

    //handling the post jobs API
    public CreateJobResponse createJob(JobRequest request) {

        String key = request.getIdempotencyKey();

        Optional<Job> existing = jobRepository.findByIdempotencyKey(key);

        if (existing.isPresent()) {
            return jobMapper.toCreateResponse(existing.get());
        }

        String jobId = UUID.randomUUID().toString();

        Job job = new Job(
                jobId,
                request.getJobType(),
                request.getPayload(),
                JobStatus.CREATED,
                key,
                LocalDateTime.now()
        );

        jobRepository.save(job);

        return jobMapper.toCreateResponse(job);
    }



    //handling getJob API
    public JobDetailResponse getJob(String jobId) {

        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new JobNotFoundException("Job not found"));

        return jobMapper.toDetailResponse(job);
    }

}
