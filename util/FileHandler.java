import com.todolist.model.Task;
import com.todolist.model.Priority;
import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class FileHandler {
    private static final String FILE_NAME = "data/tasks.txt";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    
    public static void saveTasks(List<Task> tasks) throws IOException {
        File file = new File(FILE_NAME);
        file.getParentFile().mkdirs(); // Create directory if not exists
        
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (Task task : tasks) {
                writer.write(String.format("%d|%s|%s|%s|%s|%b",
                    task.getId(),
                    task.getTitle(),
                    task.getDescription(),
                    task.getPriority().name(),
                    task.getFormattedDeadline(),
                    task.isCompleted()));
                writer.newLine();
            }
        }
    }
    
    public static List<Task> loadTasks() throws IOException {
        List<Task> tasks = new ArrayList<>();
        File file = new File(FILE_NAME);
        
        if (!file.exists()) {
            return tasks;
        }
        
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length == 6) {
                    int id = Integer.parseInt(parts[0]);
                    String title = parts[1];
                    String description = parts[2];
                    Priority priority = Priority.valueOf(parts[3]);
                    LocalDate deadline = LocalDate.parse(parts[4], DATE_FORMATTER);
                    boolean completed = Boolean.parseBoolean(parts[5]);
                    
                    Task task = new Task(title, description, priority, deadline);
                    // Note: We need to set the ID manually because Task auto-generates IDs
                    task.setCompleted(completed);
                    tasks.add(task);
                }
            }
        }
        
        return tasks;
    }
}