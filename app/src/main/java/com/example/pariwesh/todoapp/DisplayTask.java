package com.example.pariwesh.todoapp;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by pariwesh on 3/12/16.
 */
public class DisplayTask extends Activity {

    private DBHelper db;

    TextView taskText;
    TextView taskActive;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display_task);

        taskText = (TextView) findViewById(R.id.taskText);
        taskActive = (TextView) findViewById(R.id.taskActive);

        // Instantiate DBHelper
        db = new DBHelper(this);

        // Get extras from previous activity
        Bundle extras = getIntent().getExtras();

        if(extras != null){
            final int id = extras.getInt("id");
            if(id>0){
                Log.d("data", String.valueOf(id));
                updateView(id);

                FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_edit);
                fab.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // Dialog Box
                        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(DisplayTask.this);
                        alertDialog.setTitle("Edit");

                        final EditText input = new EditText(DisplayTask.this);
                        input.setText(taskText.getText());
                        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.MATCH_PARENT);
                        lp.setMargins(20,20,20,20);
                        input.setLayoutParams(lp);
                        alertDialog.setView(input);

                        //Dialog SAVE
                        alertDialog.setPositiveButton("SAVE", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Editable taskText = input.getText();
                                String text = input.getText().toString();
                                if (text.length() == 0){
                                    input.requestFocus();
                                }else{
                                    db.updateTask(text,false,id);
                                    updateView(id);
                                    dialog.dismiss();
                                }

                            }
                        });


                        //Dialog CANCEL
                        alertDialog.setNegativeButton("CANCEL",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });


                        alertDialog.show();

                        }
                    });
            }
        }

    }

    private void updateView(Integer id){
        Cursor cursor = db.getData(id);
        cursor.moveToFirst();

        String task = cursor.getString(cursor.getColumnIndex(db.TODO_COLUMN_TASK));
        boolean active =
                cursor.getInt(cursor.getColumnIndex(DBHelper.TODO_COLUMN_ACTIVE)) > 0;

        if (!cursor.isClosed()){
            cursor.close();
        }

        taskText.setText(task);
        if(active){
            taskActive.setText("Not Completed");
        }else{
            taskActive.setText("Completed");
        }

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(intent);
    }

}
