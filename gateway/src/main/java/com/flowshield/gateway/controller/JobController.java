    package com.flowshield.gateway.controller;

    import com.flowshield.common.dto.JobDetailResponse;
    import com.flowshield.common.dto.JobRequest;
    import com.flowshield.common.dto.CreateJobResponse;
    import com.flowshield.gateway.service.JobService;
    import org.springframework.http.HttpStatus;
    import org.springframework.http.ResponseEntity;
    import org.springframework.web.bind.annotation.*;

    @RestController
    @RequestMapping("/job")
    public class JobController {

        private final JobService jobService;

        public JobController(JobService jobService) {
            this.jobService = jobService;
        }

        @PostMapping
        public  ResponseEntity<CreateJobResponse>  jobPostHandler(@RequestBody JobRequest jobRequest) {

            CreateJobResponse res = jobService.createJob(jobRequest);

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(res);
        }

        @GetMapping("/{id}")
        public  ResponseEntity<JobDetailResponse>  jobGetHandler(@PathVariable String id) {
            JobDetailResponse res = jobService.getJob(id);

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(res);

        }
    }
