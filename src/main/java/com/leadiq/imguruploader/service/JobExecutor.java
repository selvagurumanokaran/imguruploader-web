package com.leadiq.imguruploader.service;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.concurrent.CompletableFuture;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import com.leadiq.imguruploader.model.ImgurRespose;
import com.leadiq.imguruploader.repository.JobRepository;

@Component
public class JobExecutor {

    private static Logger logger = LoggerFactory.getLogger(JobExecutor.class);

    @Autowired
    private JobRepository repository;

    private String imgurUploadUrl;
    private String clientId;

    public JobExecutor(@Value("${app.imgur.uploadurl}") String imgurUploadUrl,
	    @Value("${app.oauth2.clientid}") String clientId) {
	this.imgurUploadUrl = imgurUploadUrl;
	this.clientId = clientId;
    }

    @Async
    public CompletableFuture<Boolean> executeJob(String jobId, String url) {
	try {
	    File imageFile = downloadImage(new URL(url));
	    ImgurRespose response = uploadImageToImgur(imageFile);
	    if (response.isSuccess()) {
		repository.updateJobSuccess(jobId, url, response.getData().getLink());
	    } else {
		repository.updateJobFailure(jobId, url);
	    }
	} catch (Exception e) {
	    logger.error("Image upload failed for url " + url, e);
	    repository.updateJobFailure(jobId, url);
	}
	return CompletableFuture.completedFuture(true);
    }

    private ImgurRespose uploadImageToImgur(File file) {
	HttpHeaders headers = new HttpHeaders();
	headers.setContentType(MediaType.MULTIPART_FORM_DATA);
	headers.set("Authorization", "Client-ID " + clientId);
	MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
	body.add("image", new FileSystemResource(file));
	HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
	return new RestTemplate().postForEntity(imgurUploadUrl, requestEntity, ImgurRespose.class).getBody();
    }

    private File downloadImage(URL url) throws IOException {
	String path = url.getPath();
	String baseName = RandomStringUtils.randomAlphanumeric(20);
	String ext = FilenameUtils.getExtension(path);
	File destinationFile = File.createTempFile(baseName, ext);
	FileUtils.copyInputStreamToFile(url.openStream(), destinationFile);
	return destinationFile;
    }
}
