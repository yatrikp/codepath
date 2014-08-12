package com.github.yatrik.app.todopro.app;

import java.text.SimpleDateFormat;

/**
 * Created by Yatrik Mehta on 8/9/2014.
 */
public class Constants {

    public static final String SET_COMPLETION_DATE = "Set Completion Date";
    public static final String CLEAR_COMPLETION_DATE = "Clear Completion Date";

    public static final SimpleDateFormat TODO_REMINDER_FORMAT = new SimpleDateFormat("yyyyMMddHHmm");
    public static final SimpleDateFormat TODO_TODAYS_REMINDER_FORMAT = new SimpleDateFormat("HH:mm");
    public static final SimpleDateFormat TODO_REST_REMINDER_FORMAT = new SimpleDateFormat("MMM d, HH:mm");

    public static enum PRIORITY{
        LOW(0),
        NORMAL(1),
        HIGH(2);

        PRIORITY(int priority){
            this.priority = priority;

        }
        int priority;
        public int getPriority(){
            return this.priority;
        }
    }

    public static enum SORTING_ON {
        PRIORITY,
        REMIND_ON_DATE;
    }

}
