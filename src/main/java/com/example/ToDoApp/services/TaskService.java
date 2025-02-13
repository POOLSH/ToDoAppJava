package com.example.ToDoApp.services;

import com.example.ToDoApp.models.Task;
import com.example.ToDoApp.models.User;
import com.example.ToDoApp.repositories.TaskRepository;
import com.example.ToDoApp.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;
    private final UserService userService;

    public List<Task> getAllTasks() {
        User currentUser = userService.getCurrentUser();
        return taskRepository.findByUserId(currentUser.getId());
    }

    public Task createTask(Task task) {
        User currentUser = userService.getCurrentUser();
        task.setUser(currentUser);
        return taskRepository.save(task);
    }

    public Task updateTask(Long id,Task task) {
        Task taskToUpdate=taskRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("Task not found"));
        taskToUpdate.setTitle(task.getTitle());
        taskToUpdate.setDescription(task.getDescription());
        taskToUpdate.setDueDate(task.getDueDate());
        taskToUpdate.setStatus(task.getStatus());
        taskToUpdate.setUser(userService.getCurrentUser());
        return taskRepository.save(taskToUpdate);
    }

    public void deleteTask(Long id) {
        Task task=taskRepository.findById(id).orElseThrow(()-> new RuntimeException("Task not found"));
        taskRepository.delete(task);
    }
    //добавлено
    public List<Task> getFilteredTasks(String status, String title, String dueDate) {
        User currentUser = userService.getCurrentUser();
        return taskRepository.findByFilters(currentUser.getId(), status, title, dueDate);
    }
}
