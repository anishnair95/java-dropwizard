package io.dropwizard.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Saying {
    
    private long id;

    private String content;

    public Saying() {
        // Jackson deserialization
    }

    public Saying(long id, String content) {

        this.id = id;
        this.content = content;
    }

    @JsonProperty
    public long getId() {
        return id;
    }

    @JsonProperty
    public void setId(long id) {
        this.id = id;
    }

    @JsonProperty
    public String getContent() {
        return content;
    }

    @JsonProperty
    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "Saying [id=" + id + ", content=" + content + "]";
    }


    
}
