package SJToyProject.ToDoApp.Todo.dto;

import jakarta.validation.constraints.Email;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

@Data
public class TodoDTO {
    private Long code;
    private String description;
    private boolean isDone;
    @DateTimeFormat(pattern = "yyyy.MM.dd/hh:mm:ss")
    private String startDate;
    @DateTimeFormat(pattern = "yyyy.MM.dd/hh:mm:ss")
    private String endDate;
    @Email
    private String email;

    public boolean getDone(){
        return this.isDone;
    }

    public TodoDTO() {

    }
    public TodoDTO(Long code) {
        this.code = code;
    }
    public TodoDTO(String email) {
        this.email = email;
    }
    public TodoDTO(String description, String endDate, String email) {
        this.description = description;
        this.endDate = endDate;
        this.email = email;
        this.isDone = false;
    }
    public TodoDTO(Long code, String description, String startDate, String endDate, boolean isDone, String email) {
        this.code = code;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.isDone = isDone;
        this.email = email;
    }
}
