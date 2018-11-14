package com.leadiq.imguruploader.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class Job {

    private String id;
    private Date created;
    private Date finished;
    private String status;
    private Set<String> pending;
    private Set<String> complete;
    private Set<String> failed;

    public Job(String id, Date created, Set<String> pending) {
	super();
	this.id = id;
	this.created = created;
	this.pending = pending;
	this.complete = new HashSet<>();
	this.failed = new HashSet<>();
    }

    public String getId() {
	return id;
    }

    public Date getCreated() {
	return created;
    }

    public Date getFinished() {
	return finished;
    }

    public String getStatus() {
	if (this.pending.size() == 0) {
	    this.status = JobStatus.COMPLETE.getStatus();
	} else if (this.complete.size() == 0 && this.failed.size() == 0) {
	    this.status = JobStatus.PENDING.getStatus();
	} else {
	    this.status = JobStatus.INPROGRESS.getStatus();
	}
	return this.status;
    }

    public Set<String> getPending() {
	return pending;
    }

    public Set<String> getComplete() {
	return complete;
    }

    public Set<String> getFailed() {
	return failed;
    }

    public void setFinished(Date finished) {
	this.finished = finished;
    }

    public void setStatus(String status) {
	this.status = status;
    }

}
