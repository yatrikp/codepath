package com.github.yatrik.app.todo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/***
 * EditItemActivity - Activity for editing the clicked todo item from TodoAcitivity's list view
 *
 * @author  Yatrik Mehta
 */

public class EditItemActivity extends Activity {

    private EditText editItem;
    private Button saveBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);
        editItem=(EditText)findViewById(R.id.editItem);
        saveBtn=(Button)findViewById(R.id.btnSaveItem);

        Intent intent = getIntent();
        final int item_index=intent.getIntExtra(TodoActivity.ROW_ID,-1);
        String  itemToEdit=intent.getStringExtra(TodoActivity.ITEM);
        editItem.setText(itemToEdit);

        //Click Listener on Save Button Click event to pass the params back to Todo Activity
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String editedItem=editItem.getText().toString();
                Intent intent=new Intent();
                intent.putExtra(TodoActivity.ROW_ID,item_index);
                intent.putExtra(TodoActivity.ITEM,editedItem);
                setResult(TodoActivity.EDIT_TODO_REQ_CODE,intent);
                finish();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.edit_item, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
