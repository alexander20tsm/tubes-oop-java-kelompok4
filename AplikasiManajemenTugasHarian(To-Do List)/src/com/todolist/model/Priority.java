package com.todolist.model;

public enum Priority {
    LOW("Rendah"),
    MEDIUM("Sedang"),
    HIGH("Tinggi");
    
    private final String displayName;
    
    Priority(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    @Override
    public String toString() {
        return displayName;
    }
}