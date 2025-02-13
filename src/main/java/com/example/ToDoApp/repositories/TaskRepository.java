package com.example.ToDoApp.repositories;

import com.example.ToDoApp.models.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByUserId(Long id);
    //Добавлено
    @Query("SELECT t FROM Task t WHERE t.user.id = :userId " +
            "AND (:status IS NULL OR t.status = :status) " +
            "AND (:title IS NULL OR cast(t.title as string )  ILIKE concat('%',cast(:title as string ) ,'%')   )" +
            "AND (:dueDate IS NULL OR t.dueDate = :dueDate)")
    List<Task> findByFilters(@Param("userId") Long userId,
                             @Param("status") String status,
                             @Param("title") String title,
                             @Param("dueDate") String dueDate);

}
