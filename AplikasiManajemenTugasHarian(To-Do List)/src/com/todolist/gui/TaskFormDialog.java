package com.todolist.gui;

import com.todolist.model.Priority;
import com.todolist.model.Task;
import com.todolist.model.TaskManager;
import com.todolist.model.exception.InvalidDeadlineException;
import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class TaskFormDialog extends JDialog {
    private TaskManager taskManager;
    private Task taskToEdit;
    
    private JTextField titleField;
    private JTextArea descriptionArea;
    private JComboBox<Priority> priorityCombo;
    private JTextField deadlineField;
    
    public TaskFormDialog(Frame parent, TaskManager taskManager, Task taskToEdit) {
        super(parent, taskToEdit == null ? "Tambah Tugas Baru" : "Edit Tugas", true);
        this.taskManager = taskManager;
        this.taskToEdit = taskToEdit;
        
        initializeUI();
        if (taskToEdit != null) {
            loadTaskData();
        }
        
        pack();
        setLocationRelativeTo(parent);
    }
    
    private void initializeUI() {
        setLayout(new BorderLayout(10, 10));
        
        // Form Panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        
        // Title
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Judul Tugas:"), gbc);
        
        gbc.gridx = 1; gbc.gridy = 0;
        titleField = new JTextField(20);
        formPanel.add(titleField, gbc);
        
        // Description
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Deskripsi:"), gbc);
        
        gbc.gridx = 1; gbc.gridy = 1;
        descriptionArea = new JTextArea(4, 20);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(descriptionArea);
        formPanel.add(scrollPane, gbc);
        
        // Priority
        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("Prioritas:"), gbc);
        
        gbc.gridx = 1; gbc.gridy = 2;
        priorityCombo = new JComboBox<>(Priority.values());
        formPanel.add(priorityCombo, gbc);
        
        // Deadline
        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(new JLabel("Deadline (dd-MM-yyyy):"), gbc);
        
        gbc.gridx = 1; gbc.gridy = 3;
        deadlineField = new JTextField(20);
        deadlineField.setText(LocalDate.now().plusDays(7).format(
            DateTimeFormatter.ofPattern("dd-MM-yyyy")));
        formPanel.add(deadlineField, gbc);
        
        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton saveButton = new JButton("Simpan");
        JButton cancelButton = new JButton("Batal");
        
        saveButton.setBackground(new Color(46, 204, 113));
        saveButton.setForeground(Color.WHITE);
        cancelButton.setBackground(new Color(231, 76, 60));
        cancelButton.setForeground(Color.WHITE);
        
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        
        // Add panels to dialog
        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
        
        // Event Listeners
        saveButton.addActionListener(e -> saveTask());
        cancelButton.addActionListener(e -> dispose());
    }
    
    private void loadTaskData() {
        titleField.setText(taskToEdit.getTitle());
        descriptionArea.setText(taskToEdit.getDescription());
        priorityCombo.setSelectedItem(taskToEdit.getPriority());
        deadlineField.setText(taskToEdit.getFormattedDeadline());
    }
    
    private void saveTask() {
        try {
            String title = titleField.getText().trim();
            String description = descriptionArea.getText().trim();
            Priority priority = (Priority) priorityCombo.getSelectedItem();
            
            // Parse deadline
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            LocalDate deadline = LocalDate.parse(deadlineField.getText().trim(), formatter);
            
            if (taskToEdit == null) {
                // Add new task
                taskManager.addTask(title, description, priority, deadline);
                JOptionPane.showMessageDialog(this, 
                    "Tugas berhasil ditambahkan!", 
                    "Sukses", 
                    JOptionPane.INFORMATION_MESSAGE);
            } else {
                // Edit existing task
                taskManager.updateTask(taskToEdit.getId(), title, description, priority, deadline);
                JOptionPane.showMessageDialog(this, 
                    "Tugas berhasil diperbarui!", 
                    "Sukses", 
                    JOptionPane.INFORMATION_MESSAGE);
            }
            
            dispose();
            
        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(this,
                "Format tanggal salah! Gunakan format dd-MM-yyyy",
                "Error",
                JOptionPane.ERROR_MESSAGE);
        } catch (InvalidDeadlineException e) {
            JOptionPane.showMessageDialog(this,
                e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this,
                e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
}