package SJToyProject.ToDoApp.Todo.controller;

import SJToyProject.ToDoApp.Todo.domain.Todo;
import SJToyProject.ToDoApp.Todo.dto.TodoDTO;
import SJToyProject.ToDoApp.Todo.service.TodoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TodoController {
    private final TodoService todoService;
    public TodoController(TodoService todoService) {
        this.todoService = todoService;
    }

    //read
    @GetMapping("/api/todos")
    public List<Todo> getAllTodos() {
        return todoService.readAllTodo();
    }
    //create
    @PostMapping("/api/todos")
    public ResponseEntity<Object> create(@RequestBody TodoDTO todoDTO) {
        try {
            Long code = todoService.createTodo(todoDTO);
            return ResponseEntity.ok(code);
        }
        catch (IllegalArgumentException | NullPointerException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    //update
    @PutMapping("/api/todos")
    public ResponseEntity<Object> update(@RequestBody TodoDTO todoDTO) {
        try {
            todoService.updateTodo(todoDTO);
            return ResponseEntity.ok(todoDTO);
        }
        catch (IllegalArgumentException | NullPointerException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    //delete
    @DeleteMapping("/api/todos")
    public ResponseEntity<String> delete(@RequestBody TodoDTO todoDTO) {
        try {
            todoService.DeleteTodo(todoDTO.getCode());
            return ResponseEntity.ok("삭제되었습니다.");
        }
        catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/api/todos/mine")
    public List<Long> getAllCodes(@RequestBody TodoDTO todoDTO) {
        return todoService.getCodesByEmail(todoDTO.getEmail());
    }


}
