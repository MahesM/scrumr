package com.imaginea.scrumr.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;

import com.imaginea.scrumr.interfaces.IEntity;

@SuppressWarnings("serial")
@MappedSuperclass
public class AbstractEntity implements IEntity, Serializable {

    private Integer pkey;

    private Date createdOn;

    private String created_By;

    private Long version;

    @Id
    @GeneratedValue
    @Column(name = "pkey")
    public Integer getPkey() {
        return pkey;
    }

    public void setPkey(Integer pkey) {
        this.pkey = pkey;
    }

    @Column(name = "created_on")
    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    @Column(name = "created_by")
    public String getCreated_By() {
        return created_By;
    }

    public void setCreated_By(String createdBy) {
        this.created_By = createdBy;
    }

    @Version
    @Column(name = "version")
    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

}
