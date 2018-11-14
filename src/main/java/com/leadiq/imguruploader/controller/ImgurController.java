package com.leadiq.imguruploader.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.leadiq.imguruploader.error.UploadError;
import com.leadiq.imguruploader.model.JobRequest;
import com.leadiq.imguruploader.service.ImgurService;

@RestController
public class ImgurController {

    @Autowired
    private ImgurService imgurService;

    private static Logger logger = LoggerFactory.getLogger(ImgurController.class);

    @GetMapping("/v1/images")
    public ResponseEntity<?> getAllUploadedImages() {
	try {
	    return ResponseEntity.ok(imgurService.getAllUploadedImages());
	} catch (Exception e) {
	    logger.error("Failed to fetch  uploaded images.",  e);
	    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
		    .body(new UploadError("Failed to fetch  uploaded images."));
	}
    }

    @PostMapping(value = "/v1/images/upload", consumes = { APPLICATION_JSON_UTF8_VALUE }, produces = {
	    APPLICATION_JSON_UTF8_VALUE })
    public ResponseEntity<?> uploadImage(@Valid @RequestBody JobRequest jobRequest, BindingResult bindingResult) {
	if (bindingResult.hasErrors()) {
	    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(getOrderErrorDetail(bindingResult));
	}
	try {
	    return ResponseEntity.ok(imgurService.submitJob(jobRequest));
	} catch (Exception e) {
	    logger.error("Failed to upload images.", e);
	    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
		    .body(new UploadError("Failed to upload images."));
	}
    }

    @GetMapping(value="/v1/images/upload/{jobId}", produces = {
	    APPLICATION_JSON_UTF8_VALUE })
    public ResponseEntity<?> getJobStatus(@PathVariable String jobId) {
	try {
	    return ResponseEntity.ok(imgurService.getJobStatus(jobId));
	}catch(Exception e) {
	    logger.error("Failed to fetch job status.", e);
	    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
		    .body(new UploadError("Failed to fetch job status."));
	}
    }
    private UploadError getOrderErrorDetail(BindingResult bindingResult) {
	StringBuilder messageBuilder = new StringBuilder();
	bindingResult.getAllErrors().stream().forEach(fieldError -> {
	    messageBuilder.append(fieldError.getDefaultMessage() + " ");
	});
	return new UploadError(messageBuilder.toString());
    }
}
