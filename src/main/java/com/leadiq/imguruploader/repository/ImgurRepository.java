package com.leadiq.imguruploader.repository;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.leadiq.imguruploader.model.Job;
import com.leadiq.imguruploader.model.UploadedImages;
import com.leadiq.imguruploader.service.JobExecutor;

@Repository
public class ImgurRepository {

    private ConcurrentMap<String, Job> jobMap;

    @Autowired
    private JobExecutor jobExecutor;

    public ImgurRepository() {
	jobMap = new ConcurrentHashMap<>();
    }

    public UploadedImages fetchAllImages() {
	UploadedImages uploadedImages = new UploadedImages();
	jobMap.keySet().stream().forEach((key) -> {
	    uploadedImages.getUploaded().addAll(jobMap.get(key).getComplete());
	});
	return uploadedImages;
    }

    public String submitJob(Job job) {
	String jobId = job.getId();
	jobMap.put(jobId, job);
	job.getPending().forEach((url) -> {
	    jobExecutor.executeJob(jobMap, jobId, url);
	});
	return jobId;
    }

    public Job fetchJobStatus(String jobId) {
	return jobMap.get(jobId);
    }

}
