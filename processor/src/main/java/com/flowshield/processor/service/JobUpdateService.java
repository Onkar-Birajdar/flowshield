package com.flowshield.processor.service;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.flowshield.common.dto.JobStatus;
import com.flowshield.processor.model.Job;
import com.flowshield.processor.repository.ProcessorRepository;

@Service
public class JobUpdateService {

    private final ProcessorRepository processorRepository;

    public JobUpdateService(ProcessorRepository processorRepository) {
        this.processorRepository = processorRepository;
    }

    @Transactional
    public Optional<String> claimJob(String workerID) {

        Optional<Job> jobOpt =  processorRepository.findNextJobForUpdate();
        if(jobOpt.isEmpty()) return Optional.empty();
        Job job = jobOpt.get();
        
        System.out.println("###" + workerID.substring(0, 8) +
                    "] Claimed job: " + job.getJobId() + " payload: " + job.getPayload());

        job.setStatus(JobStatus.RUNNING);
        job.setLockedBy(workerID);
        job.setLockedUntil(LocalDateTime.now().plusSeconds(5));

        return Optional.of(job.getJobId());
    }

    @Transactional 
    public void completeJob(String jobId){

        Job job = processorRepository.findById(Objects.requireNonNull(jobId)).orElseThrow();

        System.out.println("@@@" + job.getLockedBy().substring(0, 8) +
                    "] Processing job: " + job.getJobId() + " payload: " + job.getPayload());

        job.setStatus(JobStatus.COMPLETED);
        job.setLockedBy(null);
        job.setLockedUntil(null);
    }

    @Transactional
    public void fail(String jobId) {

        Job job = processorRepository.findById(Objects.requireNonNull(jobId)).orElseThrow();
        System.out.println("---" + job.getLockedBy().substring(0, 8) +
                    "] Failed processing: " + job.getJobId() + " payload: " + job.getPayload());
        job.setStatus(JobStatus.FAILED);
        job.setLockedBy(null);
        job.setLockedUntil(null);
    }
}