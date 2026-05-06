package com.flowshield.processor.model;

import java.time.LocalDateTime;

import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.domain.Persistable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.flowshield.common.dto.JobStatus;

@Entity
@Table(name = "jobs")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@DynamicUpdate
public class Job implements Persistable<String> {

    @Id
    private String jobId;

    @Transient
    private boolean isNewEntity = true;

    @Override
    public boolean isNew() {
        return isNewEntity;
    }

    @Override
    public String getId() {
        return jobId;
    }

    @PostLoad
    @PostPersist
    void markNotNew() {
        this.isNewEntity = false;
    }

    private String jobType;
    private String payload;
    @Enumerated(EnumType.STRING)
    private JobStatus status;
    @Column(unique = true, nullable = false)
    private String idempotencyKey;
    private String lockedBy;
    private LocalDateTime lockedUntil;
    private LocalDateTime createdAt;
    @Version
    private Long version;
    private Integer attemptCount = 0;
    private Integer maxAttempts = 3;
    private String lastError;
    private LocalDateTime visibleAfter;
}
