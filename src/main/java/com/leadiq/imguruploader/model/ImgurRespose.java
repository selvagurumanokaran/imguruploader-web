package com.leadiq.imguruploader.model;

import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ImgurRespose {

    @JsonProperty("data")
    private ImgurData data;
    @JsonProperty("success")
    private boolean success;
    @JsonProperty("status")
    private int status;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("data")
    public ImgurData getData() {
	return data;
    }

    @JsonProperty("data")
    public void setData(ImgurData data) {
	this.data = data;
    }

    @JsonProperty("success")
    public boolean isSuccess() {
	return success;
    }

    @JsonProperty("success")
    public void setSuccess(boolean success) {
	this.success = success;
    }

    @JsonProperty("status")
    public int getStatus() {
	return status;
    }

    @JsonProperty("status")
    public void setStatus(int status) {
	this.status = status;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
	return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
	this.additionalProperties.put(name, value);
    }

}
