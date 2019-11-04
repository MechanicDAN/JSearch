package com.example.jSearch.entity;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.Date;


@MappedSuperclass
public abstract class BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Temporal(TemporalType.TIMESTAMP)
    private Date created;
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUpdate;

    @PrePersist
    public void setCreated() {
        this.created = new Date();
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    @PreUpdate
    public void setLastUpdate() {
        this.lastUpdate = new Date();
    }

    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public Date getCreated() {
        return created;
    }

    public Date getLastUpdate() {
        if (lastUpdate == null) return created;
        return lastUpdate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getTime() {
        if (this.lastUpdate != null)
            return this.lastUpdate;
        return this.created;
    }

    public String getReadableDayMonth(Date date) {
        if (date == null) return "";
        return new SimpleDateFormat("dd MMMM").format(date);
    }

    public String getReadableDateWithoutTime(Date date) {
        if (date == null) return "";
        SimpleDateFormat sdf = new SimpleDateFormat("MMMM, dd yyyy");
        return sdf.format(date);
    }


    @Override
    public String toString() {
        return "BaseEntity{" +
                "id=" + id +
                ", created=" + created +
                ", lastUpdated=" + lastUpdate +
                '}';
    }

}
