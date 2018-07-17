package com.felianneteng.lockedin;

public class Task {

    private String description;
    private boolean completed;

    private Task()
    {
    }

    public Task(String d, boolean c)
    {
        description = d;
        completed = c;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
}
