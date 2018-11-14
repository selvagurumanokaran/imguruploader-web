package com.leadiq.imguruploader.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Date;
import java.util.concurrent.ConcurrentMap;
import javax.xml.bind.DatatypeConverter;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.leadiq.imguruploader.error.UploadError;
import com.leadiq.imguruploader.model.Image;
import com.leadiq.imguruploader.model.JobStatus;

import net.bytebuddy.utility.RandomString;

import java.io.FileInputStream;

public class ImageJobExecutor {

    private static Logger logger = LoggerFactory.getLogger(ImageJobExecutor.class);

    private ObjectMapper mapper;
    private ConcurrentMap<String, Image> imageMap;

    @Value("${app.imgur.uploadurl}")
    private String imgurUploadUrl;
    @Value("${app.oauth2.clientid}")
    private String clientId;

    public ImageJobExecutor(ConcurrentMap<String, Image> imageMap) {
	this.imageMap = imageMap;
	this.mapper = new ObjectMapper();
    }

    @Async
    public void executeJob(String jobId, String url) {

	try {
	    imageMap.compute(jobId, (key, value) -> {
		if (!value.getStatus().equals(JobStatus.INPROGRESS.getStatus())) {
		    value.setStatus(JobStatus.INPROGRESS.getStatus());
		}
		return value;
	    });
	    File imageFile = downloadImage(new URL(url));
	    HttpURLConnection conn = createConnection(imageFile);
	    JsonNode response = mapper.readTree(getResponse(conn));
	    if (response.get("success").asBoolean()) {
		JsonNode data = response.get("data");
		onSuccess(jobId, url, data.get("link").asText());
	    } else {
		onFailure(jobId, url);
	    }
	} catch (IOException e) {
	    logger.error("Error occured in ImageJobExecutor ", e);
	    onFailure(jobId, url);
	}
    }

    private void onSuccess(String jobId, String url, String imgurUrl) {
	imageMap.compute(jobId, (key, value) -> {
	    value.getPending().remove(url);
	    if (value.getPending().size() == 0) {
		value.setFinished(new Date());
		value.setStatus(JobStatus.COMPLETE.getStatus());
	    }
	    value.getComplete().add(imgurUrl);
	    return value;
	});
    }

    private void onFailure(String jobId, String url) {
	imageMap.compute(jobId, (key, value) -> {
	    value.getPending().remove(url);
	    if (value.getPending().size() == 0) {
		value.setFinished(new Date());
		value.setStatus(JobStatus.COMPLETE.getStatus());
	    }
	    value.getFailed().add(url);
	    return value;
	});
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
	String baseName = FilenameUtils.getBaseName(path);
	if (baseName.equals("")) {
	    baseName = RandomString.make(20);
	}
	String ext = FilenameUtils.getExtension(path);
	if (ext.equals("")) {
	    throw new IOException("Invalid image file.");
	}
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
