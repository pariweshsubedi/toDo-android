package com.example.pariwesh.todoapp;

/**
 * Created by pariwesh on 3/13/16.
 */
public class Task {
    public int id;
    public String task;
    public boolean active;

    public Task(){
        super();
    }

    public Task(int id, String task, boolean active) {
        super();
        this.id = id;
        this.task = task;
        this.active = active;
    }

    @Override
    public String toString() {
        return String.valueOf(this.task);
    }

}
