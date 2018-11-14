package com.leadiq.imguruploader.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class Image {

    private String id;
    private Date created;
    private Date finished;
    private String status;
    private Set<String> pending;
    private Set<String> complete;
    private Set<String> failed;

    public Image(String id, Date created, String status) {
	super();
	this.id = id;
	this.created = created;
	this.status = status;
	this.pending = new HashSet<>();
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
	return status;
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
