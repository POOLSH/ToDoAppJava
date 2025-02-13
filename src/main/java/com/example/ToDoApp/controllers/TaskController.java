package com.example.ToDoApp.controllers;

import com.example.ToDoApp.models.Task;
import com.example.ToDoApp.services.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class TaskController {
    private final TaskService taskService;


    @PostMapping
    public ResponseEntity<Task> createTask(@RequestBody @Valid Task task) {
        task.setId(null); // Убеждаемся, что ID сбрасывается
        Task savedTask = taskService.createTask(task);
        System.out.println("✅ Создана задача: " + savedTask);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedTask); // 🔥 Возвращаем объект напрямую
    }

    //изменено, было отображение через getall из taskservice
    @GetMapping
    public ResponseEntity<List<Task>> getAllTasks(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String dueDate
    ) {
        List<Task> tasks = taskService.getFilteredTasks(status, title, dueDate);
        return ResponseEntity.ok(tasks);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Task> updateTask(@PathVariable Long id, @RequestBody @Valid Task task) {
        return new ResponseEntity<>(taskService.updateTask(id, task), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


}
