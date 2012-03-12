package com.imaginea.scrumr.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import com.imaginea.scrumr.interfaces.IEntity;

@SuppressWarnings("serial")
@MappedSuperclass
public class AbstractEntity implements IEntity, Serializable {

    private Integer pkey;

    private Date createdOn;    

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
}
