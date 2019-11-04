package com.example.jSearch.entity;

import javax.persistence.*;


@Entity
@Table(name = "activity")
public class Activity extends BaseEntity {

    private String userAgent;
    private String ip;
    private String expires;
    private Long totalVisitors;

    private String requestMethod;
    private String url;

    public Long getTotalVisitors() {
        return totalVisitors;
    }

    public void setTotalVisitors(Long totalVisitors) {
        this.totalVisitors = totalVisitors;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public void setUrl(String url) {
        this.url = url;
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


}
