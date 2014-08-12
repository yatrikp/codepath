package com.github.yatrik.app.todopro.app;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.github.yatrik.app.todopro.R;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by Yatrik Mehta on 8/8/2014.
 */
public class TodoAdapter extends ArrayAdapter<Todo> {

    //List for Adapter
    final private ArrayList<Todo> todos;

    //Cache for todos
    final private HashMap<Integer, Todo> todoMap;

    //database handler for persisting TodoItems
    final private DBHelper db;

    //Context
    final private Context context;

    //Sorting Order;
    private Constants.SORTING_ON sortingOn = Constants.SORTING_ON.PRIORITY;

    public TodoAdapter(Context context, ArrayList<Todo> todos) {
        super(context, R.layout.todos, todos);
        this.todos = todos;
        this.todoMap = new HashMap<Integer, Todo>();
        this.db = new DBHelper(context);
        this.context = context;
        refresh();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        final Todo todo = getItem(position);
        final ViewHolder viewHolder; // view lookup cache stored in tag

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.todos, parent, false);
            viewHolder.todoText = (TextView) convertView.findViewById(R.id.todo_description);
            viewHolder.todoSel = (CheckBox) convertView.findViewById(R.id.todo_sel);
            viewHolder.todoRemindOn = (TextView) convertView.findViewById(R.id.todo_remind_on);
            viewHolder.todoSel.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton checkbtn, boolean isChecked) {
                    Integer todoId = (Integer) checkbtn.getTag();
                    updateIsDone(todoId, isChecked);
                }
            });
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.todoText.setText(todo.getDescription());
        viewHolder.todoSel.setTag(todo.getId());
        viewHolder.todoSel.setChecked(todo.isCompleted());
        viewHolder.todoRemindOn.setText(todo.getRemindOn()==null?"":todo.getRemindOn());

        todoMap.put(todo.getId(), todo);
        if (todo.isCompleted()) {
            viewHolder.todoText.setPaintFlags(viewHolder.todoText.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            viewHolder.todoText.setPaintFlags(viewHolder.todoText.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));

        }

        // set the remindOn
        if(todo.getRemindOn() != null && !todo.getRemindOn().equals("")){
            try {
                Date remindOnDate = Constants.TODO_REMINDER_FORMAT.parse(todo.getRemindOn());
                Calendar remindOnCal = Calendar.getInstance();
                remindOnCal.setTime(remindOnDate);
                int currentDayOfYear = Calendar.getInstance().get(Calendar.DAY_OF_YEAR);
                int remindOnDayOfYear = remindOnCal.get(Calendar.DAY_OF_YEAR);
                if (currentDayOfYear==remindOnDayOfYear){
                    viewHolder.todoRemindOn.setText(Constants.TODO_TODAYS_REMINDER_FORMAT.format(remindOnCal.getTime()));
                }else {
                    viewHolder.todoRemindOn.setText(Constants.TODO_REST_REMINDER_FORMAT.format(remindOnCal.getTime()));
                }
            }
            catch(ParseException exception){
                exception.printStackTrace();
            }
        }

        // set view color as per the priority
        if(todo.getPriority()==Constants.PRIORITY.HIGH.getPriority()) {
            convertView.setBackgroundColor(context.getResources().getColor(R.color.high_priority));
        }
        else if(todo.getPriority()==Constants.PRIORITY.NORMAL.getPriority()) {
            convertView.setBackgroundColor(context.getResources().getColor(R.color.normal_priority));
        }
        else if(todo.getPriority()==Constants.PRIORITY.LOW.getPriority()) {
            convertView.setBackgroundColor(context.getResources().getColor(R.color.low_priority));
        }

        // Return the completed view to render on screen
        return convertView;
    }


    // View lookup cache
    private static class ViewHolder {
        TextView todoText;
        CheckBox todoSel;
        TextView todoRemindOn;
    }

    private void populateTodoMap() {
        this.todoMap.clear();
        for (Todo item : todos) {
            todoMap.put(item.getId(), item);
        }
    }

    public void refresh() {
        this.todos.clear();
        this.todos.addAll(db.getAll());
        performSort();
        populateTodoMap();
    }

    public Todo getById(int todoId){
        return this.todoMap.get(todoId);
    }

    public void addTodo(Todo todo) {
        int id = db.add(todo);
        todo.setId(id);
        todo.setCreatedOn(Constants.TODO_REMINDER_FORMAT.format(Calendar.getInstance().getTime()));
        this.todos.add(todo);
        this.todoMap.put(id, todo);
        performSort();
    }

    public void update(Integer id, String description, String remindOn, Integer priority) {
        Todo todo = todoMap.get(id);

        if (todo != null) {
            todo.setDescription(description);
            todo.setRemindOn(remindOn);
            todo.setPriority(priority);
            db.update(todo);
            performSort();
        }
    }

    private void updateIsDone(Integer todoId, Boolean isCompleted) {
        Todo todoItem = todoMap.get(todoId);
        if (todoItem.isCompleted() != isCompleted) {
            todoItem.setCompleted(isCompleted);
            db.update(todoItem);
            this.notifyDataSetChanged();
        }
    }

    public void deleteSelected() {
        db.deleteAllDoneTodos();
        refresh();
    }

    public void sortByPriority(){
        sortingOn = Constants.SORTING_ON.PRIORITY;
        Collections.sort(todos, Todo.PriorityComparator);
        notifyDataSetChanged();
    }

    public void sortByRemindOnDate(){
        sortingOn = Constants.SORTING_ON.REMIND_ON_DATE;
        Collections.sort(todos, Todo.RemindOnComparator);
        notifyDataSetChanged();
    }

    public void performSort(){
        performSort(sortingOn);
    }

    public void performSort(Constants.SORTING_ON sortOn){
        if(sortOn.equals(Constants.SORTING_ON.PRIORITY)){
            sortByPriority();
        }
        else if(sortOn.equals(Constants.SORTING_ON.REMIND_ON_DATE)){
            sortByRemindOnDate();
        }
    }

}
