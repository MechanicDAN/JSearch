package com.example.jSearch.entity;

import javax.persistence.*;

@Entity
@Table(name = "links")
public class Link extends BaseEntity {



    @Basic
    @Column(name = "url")
    private String URL;

    @Basic
    @Column(name = "title")
    private String title;

    public String getTitle() {
        return title;
    }

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
