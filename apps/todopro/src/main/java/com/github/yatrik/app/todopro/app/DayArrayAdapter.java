package com.github.yatrik.app.todopro.app;

/**
 * Created by Yatrik Mehta on 8/9/2014.
 */

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.yatrik.app.todopro.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import kankan.wheel.widget.adapters.AbstractWheelTextAdapter;

/**
 * Day adapter
 *
 */
public class DayArrayAdapter extends AbstractWheelTextAdapter {
    // Count of days to be shown
    private final int daysCount = 364;

    // Calendar
    Calendar calendar;

    // Local Cache for Day Array Objects
    Map<Integer, String> daysAddedToWheel = new HashMap<Integer, String>();


    /**
     * Constructor
     */
    public DayArrayAdapter(Context context, Calendar calendar) {
        super(context, R.layout.time2_day, NO_RESOURCE);
        this.calendar = calendar;

        setItemTextResource(R.id.time2_monthday);
    }

    @Override
    public View getItem(int index, View cachedView, ViewGroup parent) {
        int day = index;
        Calendar newCalendar = (Calendar) calendar.clone();
        newCalendar.roll(Calendar.DAY_OF_YEAR, day);

        View view = super.getItem(index, cachedView, parent);

        TextView monthday = (TextView) view.findViewById(R.id.time2_monthday);
        if (day == 0) {
            monthday.setText("Today");
            monthday.setTextColor(0xFF0000F0);
//                monthday.setSelected(true);
        } else {
            DateFormat format = new SimpleDateFormat("MMM d");
            monthday.setText(format.format(newCalendar.getTime()));
            monthday.setTextColor(0xFF111111);
        }
        daysAddedToWheel.put(day,Constants.TODO_REMINDER_FORMAT.format(newCalendar.getTime()));
        return view;
    }

    @Override
    public int getItemsCount() {
        return daysCount + 1;
    }

    @Override
    protected CharSequence getItemText(int index) {
        if (daysAddedToWheel.containsKey(index)){
            return daysAddedToWheel.get(index);
        }
        return "";
    }
}