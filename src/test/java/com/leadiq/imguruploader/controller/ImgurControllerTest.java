package com.leadiq.imguruploader.controller;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import com.leadiq.imguruploader.ImguruploaderWebTests;
import com.leadiq.imguruploader.error.UploadError;
import com.leadiq.imguruploader.model.Job;
import com.leadiq.imguruploader.model.JobRequest;
import com.leadiq.imguruploader.model.JobUploadResponse;
import com.leadiq.imguruploader.model.UploadedImages;
import com.leadiq.imguruploader.service.ImgurService;

@SuppressWarnings("unchecked")
public class ImgurControllerTest extends ImguruploaderWebTests {

    @InjectMocks
    @Autowired
    private ImgurController subject;

    @Mock
    private BindingResult bindingResult;

    @Mock
    private ImgurService service;

    @Test
    public void testUploadImage() {
	JobRequest request = getMockRequest();
	doReturn("123").when(service).createJob(request);
	ResponseEntity<JobUploadResponse> resEntity = (ResponseEntity<JobUploadResponse>) subject.uploadImage(request,
		bindingResult);
	assertEquals(resEntity.getStatusCode(), HttpStatus.OK);
	assertEquals("123", resEntity.getBody().getJobId());
    }

    @Test
    public void testUploadImageFailure() {
	JobRequest request = new JobRequest();
	doReturn(true).when(bindingResult).hasErrors();
	ResponseEntity<UploadError> responseError = (ResponseEntity<UploadError>) subject.uploadImage(request,
		bindingResult);
	assertEquals(responseError.getStatusCode(), HttpStatus.BAD_REQUEST);

	request.setUrls(new HashSet<>());
	responseError = (ResponseEntity<UploadError>) subject.uploadImage(request, bindingResult);
	assertEquals(responseError.getStatusCode(), HttpStatus.BAD_REQUEST);
    }

    @Test
    public void testGetUploadedImages() {
	UploadedImages uploadedImages = getMockUploadedImages();
	doReturn(uploadedImages).when(service).getAllUploadedImages();
	ResponseEntity<UploadedImages> response = (ResponseEntity<UploadedImages>) subject.getAllUploadedImages();
	assertEquals(response.getStatusCode(), HttpStatus.OK);
	assertEquals(response.getBody().getUploaded().size(), 2);
	assertEquals(response.getBody().getUploaded().contains("image1"), true);
    }

    @Test
    public void testJobStatus() {
	Job job = getMockJob();
	doReturn(job).when(service).getJobStatus("jobid");
	ResponseEntity<Job> entity = (ResponseEntity<Job>) subject.getJobStatus("jobid");
	assertEquals(entity.getStatusCode(), HttpStatus.OK);
	assertEquals(entity.getBody().getId(), "jobid");
	assertEquals(entity.getBody().getStatus(), "pending");

	job.getPending().remove("image1");
	job.getComplete().add("image1");
	entity = (ResponseEntity<Job>) subject.getJobStatus("jobid");
	assertEquals(entity.getStatusCode(), HttpStatus.OK);
	assertEquals(entity.getBody().getId(), "jobid");
	assertEquals(entity.getBody().getStatus(), "in-progress");

	job.getPending().remove("image2");
	job.getFailed().add("image2");
	entity = (ResponseEntity<Job>) subject.getJobStatus("jobid");
	assertEquals(entity.getStatusCode(), HttpStatus.OK);
	assertEquals(entity.getBody().getId(), "jobid");
	assertEquals(entity.getBody().getStatus(), "complete");
    }

    private JobRequest getMockRequest() {
	JobRequest request = new JobRequest();
	Set<String> urls = new HashSet<>();
	urls.add("https://www.bensound.com/bensound-img/clearday.jpg");
	urls.add("https://www.gstatic.com/webp/gallery3/2.png");
	request.setUrls(urls);
	return request;
    }

    private UploadedImages getMockUploadedImages() {
	UploadedImages uploadedImages = new UploadedImages();
	uploadedImages.getUploaded().add("image1");
	uploadedImages.getUploaded().add("image2");
	return uploadedImages;
    }

}
