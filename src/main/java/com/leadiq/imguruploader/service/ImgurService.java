package com.leadiq.imguruploader.service;

import java.util.Date;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.leadiq.imguruploader.model.Job;
import com.leadiq.imguruploader.model.JobRequest;
import com.leadiq.imguruploader.model.JobUploadResponse;
import com.leadiq.imguruploader.model.UploadedImages;
import com.leadiq.imguruploader.repository.ImgurRepository;

@Service
public class ImgurService {

    @Autowired
    private ImgurRepository repository;

    public UploadedImages getAllUploadedImages() {
	return repository.fetchAllImages();
    }

    public JobUploadResponse submitJob(JobRequest jobRequest) {
	String id = UUID.randomUUID().toString();
	Job job = new Job(id, new Date(), jobRequest.getUrls());
	repository.submitJob(job);
	return new JobUploadResponse(id);
    }

    public Job getJobStatus(String jobId) {
	return repository.fetchJobStatus(jobId);
    }

}
