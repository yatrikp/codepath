package com.github.yatrik.app.todopro.app;

import java.util.Comparator;
import java.util.Date;

/**
 * Created by Yatrik Mehta on 8/8/2014.
 */
public class Todo{

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

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + description.hashCode();
        result = 31 * result + (completed ? 1 : 0);
        return result;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;

        if (object == null || getClass() != object.getClass())
            return false;

        Todo todo = (Todo) object;

        if (!todo.equals(todo.description))
            return false;

        if (completed != todo.isCompleted())
            return false;

        if (id != todo.id)
            return false;

        return true;
    }

    public static Comparator<Todo> PriorityComparator = new Comparator<Todo>() {
        public int compare(Todo todo1, Todo todo2) {
            Integer priority1 = todo1.getPriority();
            Integer priority2 = todo2.getPriority();

            //ascending order
//            return priority1.compareTo(priority2);

//            descending order
            return priority2.compareTo(priority1);
        }
    };

    public static Comparator<Todo> RemindOnComparator = new Comparator<Todo>() {
        public int compare(Todo todo1, Todo todo2) {
            String remindOnDate1 = todo1.getRemindOn();
            String remindOnDate2 = todo2.getRemindOn();

            if (remindOnDate1 == null || remindOnDate1.equals("")){
                return 1;
            }
            if (remindOnDate2 == null || remindOnDate2.equals("")){
                return -1;
            }

            Long remindOnDate1Long = Long.parseLong(remindOnDate1);
            Long remindOnDate2Long = Long.parseLong(remindOnDate2);
            return remindOnDate1Long.compareTo(remindOnDate2Long);

            //ascending order
//            return priority1.compareTo(priority2);

//            descending order
//            return priority2.compareTo(priority1);
        }
    };

}
