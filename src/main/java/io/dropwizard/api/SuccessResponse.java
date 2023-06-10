package io.dropwizard.api;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SuccessResponse {


    private String id;
    private boolean success;

    public SuccessResponse() {
    }

    public SuccessResponse(String id, boolean success) {
        this.id = id;
        this.success = success;
    }

    @JsonProperty
    public String getId() {
        return id;
    }


    public void setId(String id) {
        this.id = id;
    }

    @JsonProperty
    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
