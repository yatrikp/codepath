package com.github.yatrik.app.todopro.app;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yatrik Mehta on 8/8/2014.
 */
public class DBHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    // Database
    private static final String DATABASE_NAME = "todos";

    // Table
    private static final String TABLE_TODO_ITEMS = "todo";

    // Columns
    private static final String ID = "id";
    private static final String DESCRIPTION= "description";
    private static final String COMPLETED = "completed";
    private static final String CREATED_ON = "created_on";
    private static final String REMIND_ON = "remind_on";
    private static final String PRIORITY = "priority";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_TODO_TABLE = "CREATE TABLE " + TABLE_TODO_ITEMS + "("
                + ID + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," + DESCRIPTION + " TEXT,"
                + COMPLETED + " BOOLEAN DEFAULT 'false' NOT NULL,"
                + CREATED_ON +  " TEXT,"
                + REMIND_ON +  " TEXT,"
                + PRIORITY  + " INTEGER )";
        db.execSQL(CREATE_TODO_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {

    }

    public int add(Todo todo) {
        SQLiteDatabase db=null;
        try{
            db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(DESCRIPTION, todo.getDescription());
            values.put(COMPLETED, todo.isCompleted());
            return (int)db.insertOrThrow(TABLE_TODO_ITEMS, null, values);
        }finally {
            closeConnection(null,db);
        }
    }

    public List<Todo> getAll() {
        String selectQuery="select id,description,completed,created_on,remind_on,priority from todo";
        return executeQuery(selectQuery);
    }

    private List<Todo> executeQuery(final String query){
        SQLiteDatabase db=null;
        Cursor cursor=null;
        try{
            List<Todo> items=new ArrayList<Todo>();
            db = this.getReadableDatabase();
            cursor = db.rawQuery(query, null);
            if (cursor != null){
                cursor.moveToFirst();
                if(cursor.getCount() >0 ){
                    do{
                        items.add(new Todo(cursor.getInt(0),cursor.getString(1),
                                Boolean.valueOf(cursor.getString(2)),cursor.getString(3),
                                cursor.getString(4), cursor.getInt(5)));
                    }while (cursor.moveToNext());
                }
            }
            return items;
        }finally {
            closeConnection(cursor,db);
        }

    }


    public void update(Todo todo) {
        SQLiteDatabase db=null;
        try{
            db = this.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(DESCRIPTION, todo.getDescription());
            values.put(COMPLETED, String.valueOf(todo.isCompleted()));
            values.put(REMIND_ON, todo.getRemindOn());
            values.put(PRIORITY, todo.getPriority());
            // updating row
            db.update(TABLE_TODO_ITEMS, values, ID + " = ?",
                    new String[] { String.valueOf(todo.getId()) });
        }finally {
            closeConnection(null,db);
        }
    }

    public void deleteAllDoneTodos(){
        String deleteQuery="delete from todo where "+COMPLETED+"='true'";
        executeQuery(deleteQuery);
    }

    private void closeConnection(Cursor cursor,SQLiteDatabase db){
        if(cursor!=null){
            try{
                if(!cursor.isClosed()){
                    cursor.close();
                }
            }catch (Exception e){

            }
        }
        if(db !=null){
            try{
                if(db.isOpen()){
                    db.close();
                }
            }catch (Exception e){

            }
        }

    }

}
