package com.github.yatrik.app.todo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import org.apache.commons.io.FileUtils;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/***
 * TodoActivity - Main Activity for Todo app
 *
 * @author  Yatrik Mehta
 */

public class TodoActivity extends Activity {

    public static String ITEM = "item";
    public static String ROW_ID = "rowId";
    public final static int EDIT_TODO_REQ_CODE = 8;

    private ArrayList<String> items;
    private ArrayAdapter<String> itemsAdapter;
    private ListView lvItems;

    private String TODO_FILE_NAME = "todo.txt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_main);

        readItems(); //Read Items from File

        // Getting list of to-do items
        lvItems = (ListView) findViewById(R.id.lvItems);
        items = new ArrayList<String>();

        itemsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items);
        lvItems.setAdapter(itemsAdapter);

        // registering event listener for to-do items actions - edit & delete
        setupListViewListener();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.todo_main, menu);
        return true;
    }

    // handle add button click to save the added item
    public void addTodoItem(View view){
        EditText etNewItem = (EditText) findViewById(R.id.etNewItem);
        itemsAdapter.add(etNewItem.getText().toString());
        etNewItem.setText("");
        saveItems(); // save the items by writing it to the file
    }

    private void setupListViewListener() {
        lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position,
                                           long rowId) {
                items.remove(position);
                itemsAdapter.notifyDataSetChanged();
                saveItems();
                return true;
            }
        });

        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long rowId) {
                Intent intent=new Intent(TodoActivity.this,EditItemActivity.class);
                intent.putExtra(ROW_ID, position);
                intent.putExtra(ITEM, items.get(position));
                startActivityForResult(intent, EDIT_TODO_REQ_CODE);
            }
        });
    }

    // read Items from the file
    private void readItems(){
        File filesDir = getFilesDir();
        File todoFile = new File(filesDir, TODO_FILE_NAME);
        try{
            items = new ArrayList<String>(FileUtils.readLines(todoFile));
        }
        catch(IOException exception){
            exception.printStackTrace();
        }
    }

    // save the items to the file
    private void saveItems(){
        File filesDir = getFilesDir();
        File todoFile = new File(filesDir, TODO_FILE_NAME);
        try{
            FileUtils.writeLines(todoFile, items);
        }
        catch(IOException exception){
            exception.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == EDIT_TODO_REQ_CODE && data !=null){
            int index=data.getIntExtra(ROW_ID,-1);
            String item=data.getStringExtra(ITEM);
            if(index !=-1 && item!=null){
                items.set(index,item);
                itemsAdapter.notifyDataSetChanged();
                saveItems();
            }
        }
    }
}
