package com.leadiq.imguruploader.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.leadiq.imguruploader.model.Job;
import com.leadiq.imguruploader.model.JobRequest;
import com.leadiq.imguruploader.model.JobUploadResponse;
import com.leadiq.imguruploader.model.UploadedImages;
import com.leadiq.imguruploader.repository.JobRepository;

@Service
public class ImgurService {

    @Autowired
    private JobRepository repository;

    @Autowired
    private JobExecutor jobExecutor;

    public UploadedImages getAllUploadedImages() {
	return repository.fetchAllImages();
    }

    public JobUploadResponse createJob(JobRequest jobRequest) {
	Job job = repository.createJob(jobRequest.getUrls());
	job.getPending().forEach((url) -> {
	    jobExecutor.executeJob(job.getId(), url);
	});
	return new JobUploadResponse(job.getId());
    }

    public Job getJobStatus(String jobId) {
	return repository.fetchJobStatus(jobId);
    }

}
