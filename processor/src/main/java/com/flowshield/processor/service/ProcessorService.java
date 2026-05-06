package com.flowshield.processor.service;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

@Service
public class ProcessorService {
    private final JobUpdateService jobUpdateService;

    public ProcessorService(JobUpdateService jobUpdateService) {
        this.jobUpdateService = jobUpdateService;
    }

    private final String workerID = "worker-" + UUID.randomUUID().toString();

    public void processJobs() throws InterruptedException {

        while (true) {

            Optional<String> jobIdOpt = jobUpdateService.claimJob(workerID);
            if (jobIdOpt.isEmpty())
                break;
            process(jobIdOpt.get());
        }

    }

    public void process(String jobId) {

        try {

            Thread.sleep(2000);
            jobUpdateService.completeJob(Objects.requireNonNull(jobId));
        } catch (Exception e) {

            jobUpdateService.fail(jobId);
        }
    }
}