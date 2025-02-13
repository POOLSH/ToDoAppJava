package com.example.ToDoApp.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.Entity;
import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name="Tasks")
public class Task {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY) // ЗАМЕНИЛИ SEQUENCE НА IDENTITY
    private Long id;


    @Column(name = "title", nullable = false,columnDefinition = "TEXT")
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "due_Date")
    private String dueDate;

    @Column(name="status")
    private String status;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonBackReference // ⬅️ Остановит рекурсивную сериализацию
    private User user;

}
