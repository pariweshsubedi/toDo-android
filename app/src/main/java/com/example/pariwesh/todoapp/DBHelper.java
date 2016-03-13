package com.example.pariwesh.todoapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.sql.SQLClientInfoException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by pariwesh on 3/12/16.
 */
public class DBHelper extends SQLiteOpenHelper{

    public static final String DATABASE_NAME = "todoApp.db";
    public static final String TODO_TABLE_NAME = "todo";
    public static final String TODO_COLUMN_ID = "id";
    public static final String TODO_COLUMN_TASK = "task";
    public static final String TODO_COLUMN_ACTIVE = "active";
    private HashMap hp;


    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TODO_TABLE_NAME +
                " (id integer primary key, task string, active boolean)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+ TODO_TABLE_NAME);
        onCreate(db);
    }

    public boolean insertTask(String task){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("task",task);
        contentValues.put("active", true);
        db.insert(TODO_TABLE_NAME, null, contentValues);
        return true;
    }

    public Cursor getData(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("Select * from "+TODO_TABLE_NAME+" where id="+id+"",null);
        return res;
    }

    public int getNumRows(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db,TODO_TABLE_NAME);
        return numRows;
    }

    public boolean updateTask(String task, boolean active, Integer id){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("task",task);
        contentValues.put("active",active);
        db.update(TODO_TABLE_NAME,contentValues,"id = ?", new String[] {Integer.toString(id)} );
        return true;
    }

    public boolean deleteTask(Integer id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TODO_TABLE_NAME,"id = ?", new String[]{Integer.toString(id)});
        return true;
    }

    public ArrayList<String> getAllTask(){
        ArrayList arrayList = new ArrayList<String>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from "+TODO_TABLE_NAME,null);
        res.moveToFirst();

        while (res.isAfterLast() == false){
            Log.d("data",String.valueOf(res.getInt(res.getColumnIndex(TODO_COLUMN_ID))));
            Task task = new Task();
            task.task = res.getString(res.getColumnIndex(TODO_COLUMN_TASK));
            task.id = res.getInt(res.getColumnIndex(TODO_COLUMN_ID));
            task.active = res.getInt(res.getColumnIndex(TODO_COLUMN_ID)) > 0;
            arrayList.add(task);
            Log.d("data",arrayList.toString());
            res.moveToNext();
        }

        return arrayList;
    }

}
