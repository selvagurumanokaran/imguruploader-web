package com.leadiq.imguruploader.model;

import java.util.Date;
import java.util.Set;

public class Image {

    private String id;
    private Date created;
    private Date finished;
    private JobStatus status;
    private Set<String> pending;
    private Set<String> complete;
    private Set<String> failed;
    
    public Image(String id, Date created, JobStatus status, Set<String> pending) {
	super();
	this.id = id;
	this.created = created;
	this.status = status;
	this.pending = pending;
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
    public JobStatus getStatus() {
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

}
