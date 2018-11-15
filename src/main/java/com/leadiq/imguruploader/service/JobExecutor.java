package com.leadiq.imguruploader.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.concurrent.Future;

import javax.xml.bind.DatatypeConverter;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.leadiq.imguruploader.repository.JobRepository;

import java.io.FileInputStream;

@Component
public class JobExecutor {

    private static Logger logger = LoggerFactory.getLogger(JobExecutor.class);

    @Autowired
    private ObjectMapper mapper;

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
    public Future<Boolean> executeJob(String jobId, String url) {
	try {
	    File imageFile = downloadImage(new URL(url));
	    HttpURLConnection conn = createConnection(imageFile);
	    JsonNode response = mapper.readTree(getResponse(conn));
	    if (response.get("success").asBoolean()) {
		JsonNode data = response.get("data");
		repository.updateJobSuccess(jobId, url, data.get("link").asText());
	    } else {
		repository.updateJobFailure(jobId, url);
	    }
	} catch (Exception e) {
	    logger.error("Image upload failed for url " + url, e);
	    repository.updateJobFailure(jobId, url);
	}
	return null;
    }

    private static String getResponse(HttpURLConnection conn) throws IOException {
	StringBuilder str = new StringBuilder();
	BufferedReader reader;

	if (conn.getResponseCode() != HttpStatus.OK.value()) {
	    throw new RuntimeException("Upload failed with HTTP status " + conn.getResponseCode());
	}
	reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
	String line;
	while ((line = reader.readLine()) != null) {
	    str.append(line);
	}
	reader.close();

	if (str.toString().equals("")) {
	    throw new RuntimeException("Empty response from imgur.");
	}
	return str.toString();
    }

    private String toBase64(File file) throws IOException {
	byte[] b = new byte[(int) file.length()];
	FileInputStream fs = new FileInputStream(file);
	fs.read(b);
	fs.close();
	return URLEncoder.encode(DatatypeConverter.printBase64Binary(b), "UTF-8");
    }

    private File downloadImage(URL url) throws IOException {
	String path = url.getPath();
	String baseName = RandomStringUtils.randomAlphanumeric(20);
	String ext = FilenameUtils.getExtension(path);
	File destinationFile = File.createTempFile(baseName, ext);
	FileUtils.copyInputStreamToFile(url.openStream(), destinationFile);
	return destinationFile;
    }

    private HttpURLConnection createConnection(File file) throws IOException {

	HttpURLConnection conn = (HttpURLConnection) new URL(imgurUploadUrl).openConnection();
	conn.setDoInput(true);
	conn.setDoOutput(true);
	conn.setRequestMethod("POST");
	conn.setRequestProperty("Authorization", "Client-ID " + clientId);
	conn.setReadTimeout(100000);
	conn.connect();

	OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());
	writer.write("image=" + toBase64(file));
	writer.flush();
	writer.close();

	return conn;
    }

}
