package com.tracker.collection;

import java.time.LocalDate;

public class Task {
    private int id;
    private String title;
    private String description;
    private int priority;
    private LocalDate dueDate;
    private Status status;

    public Task(int id, String title, int priority, Status status) {
        this.id = id;
        this.title = title;
        this.priority = priority;
        this.status = status;
    }

    public Task(int id, String title, String description, Priority priority, LocalDate dueDate) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.priority = priority.ordinal() + 1;
        this.dueDate = dueDate;
    }

    public Status getStatus() {
        return status;
    }

    public int getPriority() {
        return priority;
    }

    public enum Status {
        NEW,
        PROCESSING,
        FAILED,
        SUCCESS
    }

    public enum Priority {
        LOW,
        MEDIUM,
        HIGH,
        URGENT
    }

}
