package com.leadiq.imguruploader.model;

import java.util.Set;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class JobRequest {

    private String id;

    @NotNull(message = "The property 'urls' is required.")
    @Size(min = 1, message = "The property 'urls' cannot be empty. There must be at least one image url.")
    private Set<String> urls;

    public Set<String> getUrls() {
	return urls;
    }

    public String getId() {
	return id;
    }
}
