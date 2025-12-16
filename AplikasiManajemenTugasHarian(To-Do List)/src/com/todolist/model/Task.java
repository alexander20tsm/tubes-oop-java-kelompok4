package com.todolist.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Task {
    private int id;
    private String title;
    private String description;
    private Priority priority;
    private LocalDate deadline;
    private boolean isCompleted;
    
    private static int nextId = 1;
    
    public Task(String title, String description, Priority priority, LocalDate deadline) {
        this.id = nextId++;
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.deadline = deadline;
        this.isCompleted = false;
    }
    
    // Getter dan Setter
    public int getId() { return id; }
    
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public Priority getPriority() { return priority; }
    public void setPriority(Priority priority) { this.priority = priority; }
    
    public LocalDate getDeadline() { return deadline; }
    public void setDeadline(LocalDate deadline) { this.deadline = deadline; }
    
    public boolean isCompleted() { return isCompleted; }
    public void setCompleted(boolean completed) { isCompleted = completed; }
    
    public String getFormattedDeadline() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        return deadline.format(formatter);
    }
    
    @Override
    public String toString() {
        return String.format("[%s] %s - Due: %s %s", 
            priority, title, getFormattedDeadline(), 
            isCompleted ? "(Completed)" : "");
    }
}