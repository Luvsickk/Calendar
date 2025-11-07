package com.cutieprogramteam.calendar;

public class Task {
    private String name;
    private boolean isDone;

    public Task(String name, boolean isDone) {
        this.name = name;
        this.isDone = isDone;
    }

    public String getName() { return name; }
    public boolean isDone() { return isDone; }
    public void setDone(boolean done) { isDone = done; }
}
