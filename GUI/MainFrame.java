package com.todolist.gui;

import com.todolist.model.TaskManager;
import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private TaskManager taskManager;
    private TaskPanel taskPanel;
    
    public MainFrame() {
        taskManager = new TaskManager();
        initializeUI();
    }
    
    private void initializeUI() {
        setTitle("To-Do List App - Kelompok 4");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        
        // Set layout
        setLayout(new BorderLayout(10, 10));
        
        // Header Panel
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(70, 130, 180));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JLabel titleLabel = new JLabel("📝 Aplikasi Manajemen Tugas Harian");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel);
        
        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        
        JButton addButton = new JButton("➕ Tambah Tugas");
        JButton editButton = new JButton("✏ Edit Tugas");
        JButton deleteButton = new JButton("🗑 Hapus Tugas");
        JButton completeButton = new JButton("✓ Tandai Selesai");
        JButton refreshButton = new JButton("🔄 Refresh");
        
        // Style buttons
        Color buttonColor = new Color(46, 204, 113);
        for (JButton btn : new JButton[]{addButton, editButton, deleteButton, completeButton, refreshButton}) {
            btn.setBackground(buttonColor);
            btn.setForeground(Color.WHITE);
            btn.setFont(new Font("Arial", Font.BOLD, 12));
            btn.setFocusPainted(false);
        }
        
        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(completeButton);
        buttonPanel.add(refreshButton);
        
        // Task Panel (middle)
        taskPanel = new TaskPanel(taskManager);
        JScrollPane scrollPane = new JScrollPane(taskPanel);
        
        // Stats Panel (bottom)
        JPanel statsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        statsPanel.setBackground(new Color(240, 240, 240));
        statsPanel.setBorder(BorderFactory.createTitledBorder("Statistik"));
        
        JLabel totalLabel = new JLabel("Total Tugas: 0");
        JLabel pendingLabel = new JLabel("Pending: 0");
        JLabel completedLabel = new JLabel("Selesai: 0");
        
        statsPanel.add(totalLabel);
        statsPanel.add(pendingLabel);
        statsPanel.add(completedLabel);
        
        // Add components to frame
        add(headerPanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.WEST);
        add(scrollPane, BorderLayout.CENTER);
        add(statsPanel, BorderLayout.SOUTH);
        
        // Event Listeners
        addButton.addActionListener(e -> {
            TaskFormDialog dialog = new TaskFormDialog(this, taskManager, null);
            dialog.setVisible(true);
            refreshTaskList();
            updateStats(totalLabel, pendingLabel, completedLabel);
        });
        
        editButton.addActionListener(e -> {
            int selectedTaskId = taskPanel.getSelectedTaskId();
            if (selectedTaskId != -1) {
                TaskFormDialog dialog = new TaskFormDialog(this, taskManager, 
                    taskManager.getTaskById(selectedTaskId));
                dialog.setVisible(true);
                refreshTaskList();
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Pilih tugas yang akan diedit!", 
                    "Peringatan", 
                    JOptionPane.WARNING_MESSAGE);
            }
        });
        
        deleteButton.addActionListener(e -> {
            int selectedTaskId = taskPanel.getSelectedTaskId();
            if (selectedTaskId != -1) {
                int confirm = JOptionPane.showConfirmDialog(this,
                    "Apakah Anda yakin ingin menghapus tugas ini?",
                    "Konfirmasi Hapus",
                    JOptionPane.YES_NO_OPTION);
                
                if (confirm == JOptionPane.YES_OPTION) {
                    taskManager.removeTask(selectedTaskId);
                    refreshTaskList();
                    updateStats(totalLabel, pendingLabel, completedLabel);
                    JOptionPane.showMessageDialog(this, "Tugas berhasil dihapus!");
                }
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Pilih tugas yang akan dihapus!", 
                    "Peringatan", 
                    JOptionPane.WARNING_MESSAGE);
            }
        });
        
        completeButton.addActionListener(e -> {
            int selectedTaskId = taskPanel.getSelectedTaskId();
            if (selectedTaskId != -1) {
                taskManager.markAsCompleted(selectedTaskId);
                refreshTaskList();
                updateStats(totalLabel, pendingLabel, completedLabel);
                JOptionPane.showMessageDialog(this, "Tugas ditandai sebagai selesai!");
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Pilih tugas yang akan ditandai selesai!", 
                    "Peringatan", 
                    JOptionPane.WARNING_MESSAGE);
            }
        });
        
        refreshButton.addActionListener(e -> {
            refreshTaskList();
            updateStats(totalLabel, pendingLabel, completedLabel);
        });
        
        // Initial update
        updateStats(totalLabel, pendingLabel, completedLabel);
    }
    
    private void refreshTaskList() {
        taskPanel.refreshTaskList();
    }
    
    private void updateStats(JLabel totalLabel, JLabel pendingLabel, JLabel completedLabel) {
        int total = taskManager.getTaskCount();
        int pending = taskManager.getPendingTasks().size();
        int completed = taskManager.getCompletedTasks().size();
        
        totalLabel.setText("Total Tugas: " + total);
        pendingLabel.setText("Pending: " + pending);
        completedLabel.setText("Selesai: " + completed);
    }
}