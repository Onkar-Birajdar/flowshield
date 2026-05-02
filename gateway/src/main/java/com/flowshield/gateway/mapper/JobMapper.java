package com.flowshield.gateway.mapper;

import com.flowshield.common.dto.CreateJobResponse;
import com.flowshield.common.dto.JobDetailResponse;
import com.flowshield.common.dto.JobStatus;
import com.flowshield.gateway.model.Job;
import org.springframework.stereotype.Component;

@Component
public class JobMapper {
    public CreateJobResponse toCreateResponse(Job job) {
        if (job == null) return null;

        return new CreateJobResponse(
                job.getJobId(),
                JobStatus.CREATED.toString()
        );
    }

    public JobDetailResponse toDetailResponse(Job job) {
        if (job == null) return null;

        return new JobDetailResponse(
                job.getJobId(),
                job.getJobType(),
                job.getStatus(),
                job.getCreatedAt()
        );
    }
}
