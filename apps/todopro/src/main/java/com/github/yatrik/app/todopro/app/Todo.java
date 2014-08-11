package com.github.yatrik.app.todopro.app;

import java.util.Date;

/**
 * Created by Yatrik Mehta on 8/8/2014.
 */
public class Todo {

    private int id;
    private String description;
    private boolean completed;
    private String createdOn;
    private String remindOn;

    private int priority;

    public Todo(){
    }

    public Todo(int id, String description, boolean completed, String createdOn, String remindOn, int priority){
        this.id = id;
        this.description = description;
        this.completed = completed;
        this.createdOn = createdOn;
        this.remindOn = remindOn;
        this.priority = priority;
    }

    public String getRemindOn() {
        return remindOn;
    }

    public void setRemindOn(String remindOn) {
        this.remindOn = remindOn;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

}
