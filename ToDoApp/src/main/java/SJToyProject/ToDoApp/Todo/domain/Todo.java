package SJToyProject.ToDoApp.Todo.domain;

import SJToyProject.ToDoApp.Todo.dto.TodoDTO;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


@Entity
@Data
public class Todo {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long code;
    private String description;
    private boolean isDone;
    @DateTimeFormat(pattern = "yyyy.MM.dd/HH:mm:ss")
    private LocalDateTime startDate;
    @DateTimeFormat(pattern = "yyyy.MM.dd/HH:mm:ss")
    private LocalDateTime endDate;
    private String email;

    public Todo(TodoDTO todoDTO) {
        this.isDone = false;
        this.description = todoDTO.getDescription();
        this.startDate = LocalDateTime.now();
        this.endDate = LocalDateTime.parse(todoDTO.getEndDate(), DateTimeFormatter.ofPattern("yyyy.MM.dd/HH:mm:ss"));
        this.email = todoDTO.getEmail();

    }

    public void update(TodoDTO todoDTO) {
        this.isDone = todoDTO.getDone();
        this.description = todoDTO.getDescription();
        this.startDate = LocalDateTime.parse(todoDTO.getStartDate(), DateTimeFormatter.ofPattern("yyyy.MM.dd/HH:mm:ss"));
        this.endDate = LocalDateTime.parse(todoDTO.getEndDate(), DateTimeFormatter.ofPattern("yyyy.MM.dd/HH:mm:ss"));
        this.email = todoDTO.getEmail();
    }
    public Todo() {

    }
}
