package com.flowshield.processor.scheduler;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.flowshield.processor.service.ProcessorService;

@Component
public class JobScheduler {

    private final ProcessorService processorService;

    public JobScheduler(ProcessorService processorService) {
        this.processorService = processorService;
    }

    @Scheduled(fixedDelay = 5000)
    public void runProcessor() {
        try {
            processorService.processJobs();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}