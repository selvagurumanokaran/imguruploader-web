package com.leadiq.imguruploader.model;

import java.util.HashSet;
import java.util.Set;

public class UploadedImages {
    private Set<String> uploaded;

    public Set<String> getUploaded() {
	return uploaded;
    }

    public UploadedImages() {
	super();
	this.uploaded = new HashSet<>();
    }

}
