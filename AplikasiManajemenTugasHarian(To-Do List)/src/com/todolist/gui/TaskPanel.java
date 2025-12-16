package com.todolist.gui;

import com.todolist.model.Task;
import com.todolist.model.TaskManager;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class TaskPanel extends JPanel {
    private TaskManager taskManager;
    private JTable taskTable;
    private DefaultTableModel tableModel;
    
    public TaskPanel(TaskManager taskManager) {
        this.taskManager = taskManager;
        initializeUI();
        refreshTaskList();
    }
    
    private void initializeUI() {
        setLayout(new BorderLayout());
        
        // Table model
        String[] columns = {"ID", "Judul", "Deskripsi", "Prioritas", "Deadline", "Status"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table non-editable
            }
        };
        
        taskTable = new JTable(tableModel);
        taskTable.setRowHeight(30);
        taskTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        taskTable.setFont(new Font("Arial", Font.PLAIN, 11));
        
        // Set column widths
        taskTable.getColumnModel().getColumn(0).setPreferredWidth(50);  // ID
        taskTable.getColumnModel().getColumn(1).setPreferredWidth(150); // Judul
        taskTable.getColumnModel().getColumn(2).setPreferredWidth(200); // Deskripsi
        taskTable.getColumnModel().getColumn(3).setPreferredWidth(80);  // Prioritas
        taskTable.getColumnModel().getColumn(4).setPreferredWidth(100); // Deadline
        taskTable.getColumnModel().getColumn(5).setPreferredWidth(80);  // Status
        
        // Add table to scroll pane
        JScrollPane scrollPane = new JScrollPane(taskTable);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Daftar Tugas"));
        
        add(scrollPane, BorderLayout.CENTER);
    }
    
    public void refreshTaskList() {
        // Clear table
        tableModel.setRowCount(0);
        
        // Get all tasks
        List<Task> tasks = taskManager.getAllTasks();
        
        // Add tasks to table
        for (Task task : tasks) {
            String status = task.isCompleted() ? "Selesai" : "Pending";
            Color rowColor = task.isCompleted() ? new Color(200, 255, 200) : 
                           (task.getPriority().toString().equals("Tinggi") ? 
                            new Color(255, 200, 200) : Color.WHITE);
            
            tableModel.addRow(new Object[]{
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getPriority().toString(),
                task.getFormattedDeadline(),
                status
            });
        }
    }
    
    public int getSelectedTaskId() {
        int selectedRow = taskTable.getSelectedRow();
        if (selectedRow != -1) {
            return (int) tableModel.getValueAt(selectedRow, 0);
        }
        return -1;
    }
}