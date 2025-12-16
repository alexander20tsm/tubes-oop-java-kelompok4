package com.todolist.model;

import com.todolist.model.exception.InvalidDeadlineException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class TaskManager {
    private List<Task> tasks;
    
    public TaskManager() {
        tasks = new ArrayList<>();
        loadSampleData(); // Data contoh untuk testing
    }
    
    private void loadSampleData() {
        try {
            addTask("Belajar Java Swing", "Mengerjakan tugas besar OOP", Priority.HIGH, 
                    LocalDate.now().plusDays(3));
            addTask("Beli bahan makanan", "Sayur dan buah", Priority.MEDIUM, 
                    LocalDate.now().plusDays(1));
            addTask("Olahraga pagi", "Lari 30 menit", Priority.LOW, 
                    LocalDate.now().plusDays(7));
        } catch (InvalidDeadlineException e) {
            System.err.println("Error loading sample data: " + e.getMessage());
        }
    }
    
    public void addTask(String title, String description, Priority priority, LocalDate deadline) 
            throws InvalidDeadlineException {
        
        // Validasi deadline tidak boleh di masa lalu
        if (deadline.isBefore(LocalDate.now())) {
            throw new InvalidDeadlineException("Deadline tidak boleh di masa lalu!");
        }
        
        // Validasi title tidak boleh kosong
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Judul tugas tidak boleh kosong!");
        }
        
        Task task = new Task(title, description, priority, deadline);
        tasks.add(task);
    }
    
    public boolean removeTask(int taskId) {
        return tasks.removeIf(task -> task.getId() == taskId);
    }
    
    public boolean updateTask(int taskId, String title, String description, 
                             Priority priority, LocalDate deadline) throws InvalidDeadlineException {
        
        if (deadline.isBefore(LocalDate.now())) {
            throw new InvalidDeadlineException("Deadline tidak boleh di masa lalu!");
        }
        
        for (Task task : tasks) {
            if (task.getId() == taskId) {
                task.setTitle(title);
                task.setDescription(description);
                task.setPriority(priority);
                task.setDeadline(deadline);
                return true;
            }
        }
        return false;
    }
    
    public void markAsCompleted(int taskId) {
        for (Task task : tasks) {
            if (task.getId() == taskId) {
                task.setCompleted(true);
                break;
            }
        }
    }
    
    public List<Task> getAllTasks() {
        return new ArrayList<>(tasks);
    }
    
    public List<Task> getPendingTasks() {
        return tasks.stream()
                .filter(task -> !task.isCompleted())
                .collect(Collectors.toList());
    }
    
    public List<Task> getCompletedTasks() {
        return tasks.stream()
                .filter(Task::isCompleted)
                .collect(Collectors.toList());
    }
    
    public List<Task> getTasksByPriority(Priority priority) {
        return tasks.stream()
                .filter(task -> task.getPriority() == priority)
                .collect(Collectors.toList());
    }
    
    public List<Task> sortByDeadline() {
        return tasks.stream()
                .sorted(Comparator.comparing(Task::getDeadline))
                .collect(Collectors.toList());
    }
    
    public List<Task> sortByPriority() {
        return tasks.stream()
                .sorted((t1, t2) -> t2.getPriority().compareTo(t1.getPriority()))
                .collect(Collectors.toList());
    }
    
    public Task getTaskById(int taskId) {
        return tasks.stream()
                .filter(task -> task.getId() == taskId)
                .findFirst()
                .orElse(null);
    }
    
    public int getTaskCount() {
        return tasks.size();
    }
}