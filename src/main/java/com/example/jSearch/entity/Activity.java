package com.example.jSearch.entity;

import javax.persistence.*;


@Entity
@Table(name = "activity",
        indexes = {@Index(name = "param_index", columnList = "parameters")})
public class Activity extends BaseEntity {

    private String userAgent;
    private String ip;
    private String links;
    private String expires;
    private String parameters;
    private String requestMethod;
    private String url;

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setLinks(String links) {
        this.links = links;
    }

    public void setExpires(String expires) {
        this.expires = expires;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public void setRequestMethod(String requestMethod) {
        this.requestMethod = requestMethod;
    }

    public void setParameters(String parameters) {
        this.parameters = parameters;
    }

    public String getParameters() {
        return parameters;
    }

    public String getUrl() {
        return url;
    }

    public String getExpires() {
        return expires;
    }

    public String getIp() {
        return ip;
    }

    public String getRequestMethod() {
        return requestMethod;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public String getLinks() {
        return links;
    }
}
