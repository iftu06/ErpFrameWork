package com.example.demo.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

/**
 * Created by Divineit-Iftekher on 8/16/2017.
 */
@Embeddable
public class AccessTime {

    private String createdBy;
    private String updatedBy;
    private Date created;
    private Date updated;

   public  AccessTime(){

    }

   public  AccessTime(Date created,Date updated){
        this.created = created;
        this.updated = updated;
    }

    @Column(name = "created_by")
    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    @Column(name = "updated_by")
    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    @Column(name = "created")
    @Temporal(TemporalType.DATE)
    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    @Column(name = "updated")
    @Temporal(TemporalType.DATE)
    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }
}
