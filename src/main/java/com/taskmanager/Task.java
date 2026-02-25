package com.taskmanager;

public class Task {
    // These are "fields" - the data we want to keep for each task
    private int id;
    private String description;
    private boolean isDone;

    // This is the "Constructor" - it runs when we create a new task
    public Task(int id, String description) {
        this.id = id;
        this.description = description;
        this.isDone = false; // By default, a new task isn't finished yet
    }

    // This tells Java how to print the task as text
    @Override
    public String toString() {
        String status = isDone ? "[X]" : "[ ]";
        return id + ". " + status + " " + description;
    }
}
