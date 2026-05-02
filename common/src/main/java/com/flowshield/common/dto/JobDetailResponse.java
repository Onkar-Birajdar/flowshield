package com.flowshield.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JobDetailResponse {

    private String jobId;
    private String jobType;
    private JobStatus status;
    private LocalDateTime createdAt;
}