package com.leadiq.imguruploader.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;

import javax.xml.bind.DatatypeConverter;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.leadiq.imguruploader.model.Image;
import java.io.FileInputStream;

@Service
public class ImageJobExecutor {

    private ObjectMapper mapper;
    private ConcurrentMap<String, Image> imageMap;

    public ImageJobExecutor(ConcurrentMap<String, Image> imageMap) {
	this.imageMap = imageMap;
	this.mapper = new ObjectMapper();
    }

    @Async
    public void executeJob(String jobId, String url) {

	try {
	    File imageFile = downloadImage(new URL(url));
	    HttpURLConnection conn = createConnection(imageFile);
	    JsonNode response = mapper.readTree(getResponse(conn));
	    if (response.get("success").asBoolean()) {
		onSuccess(jobId, url);
	    } else {
		onFailure(jobId, url);
	    }
	} catch (IOException e) {
	    onFailure(jobId, url);
	}
    }

    private void onSuccess(String jobId, String url) {
	imageMap.compute(jobId, (key, value) -> {
	    value.getPending().remove(url);
	    value.getComplete().add(url);
	    return value;
	});
    }

    private void onFailure(String jobId, String url) {
	imageMap.compute(jobId, (key, value) -> {
	    value.getPending().remove(url);
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
	File destinationFile = File.createTempFile(FilenameUtils.getBaseName(path), FilenameUtils.getExtension(path));
	FileUtils.copyInputStreamToFile(url.openStream(), destinationFile);
	return destinationFile;
    }

    private HttpURLConnection createConnection(File file) throws IOException {
	HttpURLConnection conn = (HttpURLConnection) new URL("https://api.imgur.com/3/image").openConnection();
	conn.setDoInput(true);
	conn.setDoOutput(true);
	conn.setRequestMethod("POST");
	conn.setRequestProperty("Authorization", "Client-ID f0d245d25bb3313");
	conn.setReadTimeout(100000);
	conn.connect();

	OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());
	writer.write("image=" + toBase64(file));
	writer.flush();
	writer.close();

	return conn;
    }

    public Map<String, Image> getImageMap() {
	return imageMap;
    }

}
