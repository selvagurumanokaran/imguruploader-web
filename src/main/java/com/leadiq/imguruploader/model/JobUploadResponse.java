package com.leadiq.imguruploader.model;

public class JobUploadResponse {

    public JobUploadResponse(String jobId) {
	super();
	this.jobId = jobId;
    }

    private String jobId;

    public String getJobId() {
	return jobId;
    }

}
