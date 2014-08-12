package com.github.yatrik.app.todopro.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.github.yatrik.app.todopro.R;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import kankan.wheel.widget.WheelView;
import kankan.wheel.widget.adapters.ArrayWheelAdapter;
import kankan.wheel.widget.adapters.NumericWheelAdapter;

public class TodoActivity extends Activity {

    private TodoAdapter itemAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_main);

        ListView listView;
        listView = (ListView) findViewById(R.id.lvItems);

        //Wiring Items read from files with Adapter to keep track of changes
        itemAdapter = new TodoAdapter(this, new ArrayList<Todo>());
        listView.setAdapter(itemAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long row_id) {
                Todo item = (Todo)adapterView.getItemAtPosition(pos);
                showDialog(true,item.getId());
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.todo, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id){
            case R.id.action_add :
                showDialog(false,-1);
                break;
            case R.id.action_delete:
                itemAdapter.deleteSelected();
                break;
            case R.id.ic_action_sort_on_priority:
                itemAdapter.performSort(Constants.SORTING_ON.PRIORITY);
                break;
            case R.id.ic_action_sort_on_completion_date:
                itemAdapter.performSort(Constants.SORTING_ON.REMIND_ON_DATE);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /***
     * Shows dialog for Add/Edit TodoItem
     * @param isEdit Open Dialog for Add or Edit
     * @param todoId is mandatory only when isEdit is true
     */
    private void showDialog(final boolean isEdit,final int todoId){
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        final View promptView = layoutInflater.inflate(R.layout.dialog_view, null);
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setView(promptView);
        final EditText input = (EditText) promptView.findViewById(R.id.dialog_item);
        final Button saveButton = (Button) promptView.findViewById(R.id.btnSaveTodo);
        final Button cancelButton = (Button) promptView.findViewById(R.id.btnCancelTodo);

        // radio group button for priority
        final RadioGroup radioPriority = (RadioGroup) promptView.findViewById(R.id.radio_priority);

        // remindOn views
        final View remindOnDateView = promptView.findViewById(R.id.remind_on_date_view);
        final WheelView remindOnDay = (WheelView) promptView.findViewById(R.id.day);
        final WheelView remindOnHour = (WheelView) promptView.findViewById(R.id.hour);
        final WheelView remindOnMin = (WheelView) promptView.findViewById(R.id.mins);

        // set Completion Date Text View
        final TextView setCompletionDateTextView = (TextView) promptView.findViewById(R.id.set_completion_date);
        final Boolean completionDateVisible = false;

        // adding datetime wheel to view
        NumericWheelAdapter hourAdapter = new NumericWheelAdapter(this, 0, 23);
        hourAdapter.setItemResource(R.layout.wheel_text_item);
        hourAdapter.setItemTextResource(R.id.text);
        remindOnHour.setViewAdapter(hourAdapter);

        NumericWheelAdapter minAdapter = new NumericWheelAdapter(this, 0, 59, "%02d");
        minAdapter.setItemResource(R.layout.wheel_text_item);
        minAdapter.setItemTextResource(R.id.text);
        remindOnMin.setViewAdapter(minAdapter);
        remindOnMin.setCyclic(true);

        // set current time
        Calendar cal = Calendar.getInstance(Locale.US);
        remindOnHour.setCurrentItem(cal.get(Calendar.HOUR_OF_DAY));
        remindOnMin.setCurrentItem(cal.get(Calendar.MINUTE));

        remindOnDay.setViewAdapter(new DayArrayAdapter(this, cal));

        if(isEdit) {
            Todo item = itemAdapter.getById(todoId);

            // set description for edit
            input.setText(item.getDescription());

            // set priority for edit
            if (item.getPriority() == Constants.PRIORITY.HIGH.getPriority()) {
                setRadioButtonColor(promptView.findViewById(R.id.radio_priority_high));
            } else if (item.getPriority() == Constants.PRIORITY.NORMAL.getPriority()) {
                setRadioButtonColor(promptView.findViewById(R.id.radio_priority_normal));
            } else if (item.getPriority() == Constants.PRIORITY.LOW.getPriority()) {
                setRadioButtonColor(promptView.findViewById(R.id.radio_priority_low));
            }

            if (item.getRemindOn() !=  null && !item.getRemindOn().equals("")){
                remindOnDateView.setVisibility(View.VISIBLE);
                setCompletionDateTextView.setText(Constants.CLEAR_COMPLETION_DATE);
            }

            if (item.getRemindOn() != null && !item.getRemindOn().equals("")){
                try {
                    Date remindOnDate = Constants.TODO_REMINDER_FORMAT.parse(item.getRemindOn());
                    Calendar remindOnCal = Calendar.getInstance();
                    remindOnCal.setTime(remindOnDate);

                    if (remindOnCal.get(Calendar.DAY_OF_YEAR) == Calendar.getInstance().get(Calendar.DAY_OF_YEAR)) {
                        remindOnDay.setCurrentItem(0);
                    } else if (remindOnCal.get(Calendar.DAY_OF_YEAR) > Calendar.getInstance().get(Calendar.DAY_OF_YEAR)) {
                        remindOnDay.setCurrentItem(remindOnCal.get(Calendar.DAY_OF_YEAR)
                                - Calendar.getInstance().get(Calendar.DAY_OF_YEAR));
                    }
                    remindOnHour.setCurrentItem(remindOnCal.get(Calendar.HOUR_OF_DAY));
                    remindOnMin.setCurrentItem(remindOnCal.get(Calendar.MINUTE));
                } catch (ParseException exception) {
                    exception.printStackTrace();
                }
            }
        }else{
//            textView.setText("Add:");
            // setting the default radio priority to normal for Add flow
            setRadioButtonColor(promptView.findViewById(R.id.radio_priority_normal));
        }

        alertDialogBuilder.setCancelable(true);
        final AlertDialog alertD = alertDialogBuilder.create();

        input.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    alertD.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                }
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                // getting description
                Editable editable = input.getText();
                String inputString = editable.toString();

                // getting priority selected button
                int prioritySelected = Constants.PRIORITY.NORMAL.getPriority();
                int prioritySelectedId = radioPriority.getCheckedRadioButtonId();
                if (prioritySelectedId == -1){
                    //no item selected
                }
                else{
                    switch (prioritySelectedId){
                        case R.id.radio_priority_high:
                            prioritySelected = Constants.PRIORITY.HIGH.getPriority();
                            break;
                        case R.id.radio_priority_normal:
                            prioritySelected = Constants.PRIORITY.NORMAL.getPriority();
                            break;
                        case R.id.radio_priority_low:
                            prioritySelected = Constants.PRIORITY.LOW.getPriority();
                            break;
                    }
                }

                Calendar selectedCalendar = Calendar.getInstance();
                boolean completionDateSet = false;
                // get the completion date only if its set
                if(setCompletionDateTextView.getText().toString().equalsIgnoreCase(Constants.CLEAR_COMPLETION_DATE)) {
                   // getting the remindOn selected values
                    String daySelected = ((DayArrayAdapter) remindOnDay.getViewAdapter()).getItemText(remindOnDay.getCurrentItem()).toString();
                    String hourSelected = ((NumericWheelAdapter) remindOnHour.getViewAdapter()).getItemText(remindOnHour.getCurrentItem()).toString();
                    String minSelected = ((NumericWheelAdapter) remindOnMin.getViewAdapter()).getItemText(remindOnMin.getCurrentItem()).toString();
                    try {
                        Date date = Constants.TODO_REMINDER_FORMAT.parse(daySelected);
                        selectedCalendar.setTime(date);
                        selectedCalendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(hourSelected));
                        selectedCalendar.set(Calendar.MINUTE, Integer.parseInt(minSelected));
                    } catch (ParseException exception) {
                        exception.printStackTrace();
                    }
                    completionDateSet = true;
                }

                if(inputString!=null && !inputString.trim().equals("")){
                    if(isEdit){
                        itemAdapter.update(todoId,inputString,
                                completionDateSet?Constants.TODO_REMINDER_FORMAT.format(selectedCalendar.getTime()):"",
                                prioritySelected);

                    }else{
                        Todo todo=new Todo();
                        todo.setDescription(inputString);
                        todo.setRemindOn(completionDateSet?Constants.TODO_REMINDER_FORMAT.format(selectedCalendar.getTime()):"");
                        todo.setPriority(prioritySelected);
                        itemAdapter.addTodo(todo);
                    }
                }
                alertD.cancel();
//                itemAdapter.refresh();
            }
        });


        cancelButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                alertD.cancel();
            }
        });


        setCompletionDateTextView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                String completionDateText = ((TextView)arg0).getText().toString();
                if (completionDateText.equalsIgnoreCase("Set Completion Date")) {
                    remindOnDateView.setVisibility(View.VISIBLE);
                    ((TextView)arg0).setText(Constants.CLEAR_COMPLETION_DATE);
                }
                else{
                    remindOnDateView.setVisibility(View.GONE);
                    ((TextView)arg0).setText(Constants.SET_COMPLETION_DATE);
                }
            }

        });

        alertD.show();
    }


    public void onRadioButtonClicked(View view){
        setRadioButtonColor(view);
    }

    private void setRadioButtonColor(View view){
        RadioGroup radioGroup = (RadioGroup)view.getParent();
        view.setPressed(true);
        view.setSelected(true);
        if (view.getId()==R.id.radio_priority_high) {
            radioGroup.findViewById(R.id.radio_priority_normal).setPressed(false);
            radioGroup.findViewById(R.id.radio_priority_normal).setSelected(false);
            radioGroup.findViewById(R.id.radio_priority_low).setPressed(false);
            radioGroup.findViewById(R.id.radio_priority_low).setSelected(false);
        }
        else if (view.getId()==R.id.radio_priority_normal) {
            radioGroup.findViewById(R.id.radio_priority_high).setPressed(false);
            radioGroup.findViewById(R.id.radio_priority_high).setSelected(false);
            radioGroup.findViewById(R.id.radio_priority_low).setPressed(false);
            radioGroup.findViewById(R.id.radio_priority_low).setSelected(false);
        }
        else if (view.getId()==R.id.radio_priority_low) {
            radioGroup.findViewById(R.id.radio_priority_normal).setPressed(false);
            radioGroup.findViewById(R.id.radio_priority_normal).setSelected(false);
            radioGroup.findViewById(R.id.radio_priority_high).setPressed(false);
            radioGroup.findViewById(R.id.radio_priority_high).setSelected(false);
        }
    }


}
