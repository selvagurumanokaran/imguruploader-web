package com.leadiq.imguruploader.model;

public enum JobStatus {
    PENDING("pending"), INPROGRESS("in-progress"), COMPLETE("complete");
    private String status;

    public String getStatus() {
	return this.status;
    }

    private JobStatus(String status) {
	this.status = status;
    }
}
