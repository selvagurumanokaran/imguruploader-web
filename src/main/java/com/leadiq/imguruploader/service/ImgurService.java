package com.leadiq.imguruploader.service;

import java.util.Date;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import javax.validation.Valid;

import org.springframework.stereotype.Service;
import com.leadiq.imguruploader.model.Image;
import com.leadiq.imguruploader.model.JobRequest;
import com.leadiq.imguruploader.model.JobStatus;
import com.leadiq.imguruploader.model.JobUploadResponse;
import com.leadiq.imguruploader.model.UploadedImages;

@Service
public class ImgurService {

    private ConcurrentMap<String, Image> imageMap;

    private ImageJobExecutor jobExecutor;

    public ImgurService() {
	imageMap = new ConcurrentHashMap<>();
	jobExecutor = new ImageJobExecutor(imageMap);
    }

    public UploadedImages getAllUploadedImages() {
	UploadedImages uploadedImages = new UploadedImages();
	imageMap.keySet().stream().forEach((key) -> {
	    uploadedImages.getUploaded().addAll(imageMap.get(key).getComplete());
	});
	return uploadedImages;
    }

    public JobUploadResponse submitJob(@Valid JobRequest jobRequest) {
	String id = getNewId();
	Image image = new Image(id, new Date(), JobStatus.PENDING.getStatus());
	imageMap.put(id, image);

	jobRequest.getUrls().stream().parallel().forEach((url) -> {
	    image.getPending().add(url);
	    jobExecutor.executeJob(id, url);
	});
	return new JobUploadResponse(id);
    }

    private String getNewId() {
	return UUID.randomUUID().toString();
    }

    public Image getJobStatus(String jobId) {
	return imageMap.get(jobId);
    }

}
