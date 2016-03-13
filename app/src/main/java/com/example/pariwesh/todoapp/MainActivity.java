package com.example.pariwesh.todoapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ListView listView;
    DBHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("To Do App");
        setSupportActionBar(toolbar);
        db = new DBHelper(this);
        updateView(db);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setImageDrawable(getResources().getDrawable(R.drawable.add_button));
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Dialog Box
                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);



                final EditText input = new EditText(MainActivity.this);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                lp.setMargins(20,20,20,20);
                input.setLayoutParams(lp);

                alertDialog.setView(input);
                alertDialog.setTitle("New");
                    //Dialog SAVE
                    alertDialog.setPositiveButton("ADD", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Editable taskText = input.getText();
                            String text = input.getText().toString();
                            if (text.length() == 0){
                                input.requestFocus();
                            }else{
                                db.insertTask(text);
                                updateView(db);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void updateView(final DBHelper db){
        // Get or create a DB

        ArrayList arrayList = db.getAllTask();
        ArrayAdapter arrayAdapter =
                new ArrayAdapter(this,android.R.layout.simple_list_item_1,arrayList);

        Log.d("data",arrayList.toString());

        //List View for Tasks
        listView = (ListView)findViewById(R.id.TaskslistView);
        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Task task = (Task) parent.getItemAtPosition(position);

                Bundle dataBundle = new Bundle();
                Log.d("data", String.valueOf(task.id));
                dataBundle.putInt("id", task.id);

                Intent intent = new Intent(getApplicationContext(), DisplayTask.class);
                intent.putExtras(dataBundle);
                startActivity(intent);
            }
        });

        listView.setOnItemLongClickListener(new OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view,
                                           int pos, long id) {

                final Task task = (Task) parent.getItemAtPosition(pos);
                final AlertDialog.Builder removeDialog = new AlertDialog.Builder(MainActivity.this);
                removeDialog .setTitle("Are you sure you want to delete this task ?");

                //Dialog SAVE
                removeDialog.setPositiveButton("DELETE", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        db.deleteTask(task.id);
                        updateView(db);
                        dialog.dismiss();
                    }
                });


                //Dialog CANCEL
                removeDialog.setNegativeButton("CANCEL",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                removeDialog.show();
                return true;
            }
        });

    }

}
