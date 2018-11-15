package com.leadiq.imguruploader.repository;

import java.util.Date;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.springframework.stereotype.Repository;

import com.leadiq.imguruploader.model.Job;
import com.leadiq.imguruploader.model.UploadedImages;

@Repository
public class JobRepository {

    private ConcurrentMap<String, Job> jobMap;

    public JobRepository() {
	jobMap = new ConcurrentHashMap<>();
    }

    public UploadedImages fetchAllImages() {
	UploadedImages uploadedImages = new UploadedImages();
	jobMap.keySet().stream().forEach((key) -> {
	    uploadedImages.getUploaded().addAll(jobMap.get(key).getComplete());
	});
	return uploadedImages;
    }

    public Job fetchJobStatus(String jobId) {
	return jobMap.get(jobId);
    }

    public Job createJob(Set<String> pending) {
	String id = UUID.randomUUID().toString();
	Job job = new Job(id, new Date(), pending);
	jobMap.put(id, job);
	return job;
    }

    public void updateJobSuccess(String jobId, String url, String imgurUrl) {
	jobMap.compute(jobId, (key, value) -> {
	    value.getPending().remove(url);
	    if (value.getPending().size() == 0) {
		value.setFinished(new Date());
	    }
	    value.getComplete().add(imgurUrl);
	    return value;
	});
    }

    public void updateJobFailure(String jobId, String url) {
	jobMap.compute(jobId, (key, value) -> {
	    value.getPending().remove(url);
	    if (value.getPending().size() == 0) {
		value.setFinished(new Date());
	    }
	    value.getFailed().add(url);
	    return value;
	});

    }

}
