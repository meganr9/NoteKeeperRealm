package com.example.bhanu.notekeeper;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Megan Reiffer
 */

public class Note extends RealmObject
{

    @PrimaryKey
    public   String id;
   public   String subject, priority, status;
    public Date upDateTime;


    public Note(String id, String priority, String status, String subject, Date upDateTime) {
        this.id = id;
        this.priority = priority;
        this.status = status;
        this.subject = subject;
        this.upDateTime = upDateTime;
    }

    public Date getUpDateTime() {
        return upDateTime;
    }

    public void setUpDateTime(Date upDateTime) {
        this.upDateTime = upDateTime;
    }


    @Override
    public String toString() {
        return super.toString();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Note() {
    }
}