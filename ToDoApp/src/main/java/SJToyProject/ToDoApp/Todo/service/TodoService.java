package SJToyProject.ToDoApp.Todo.service;

import SJToyProject.ToDoApp.Todo.domain.Todo;
import SJToyProject.ToDoApp.Todo.dto.TodoDTO;
import SJToyProject.ToDoApp.Todo.repository.TodoRepository;
import jakarta.transaction.Transactional;

import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Objects;

@Transactional
public class TodoService {
    private final TodoRepository todoRepository;
    public TodoService(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    public Long createTodo(TodoDTO todoDTO) {
        checkCreateNull(todoDTO);
        try {
            Todo todo = new Todo(todoDTO);
            return todoRepository.create(todo);
        }
        catch (DateTimeParseException e) {
            throw new IllegalArgumentException("날짜 형식을 다시 맞춰주세요! -> 년.월.일/시:분:초");
        }
    }

    public List<Todo> readAllTodo() {
        return todoRepository.findAll();
    }
    public void updateTodo(TodoDTO todoDTO) {
        checkUpdateNull(todoDTO);
        Todo toFix = findByCode(todoDTO.getCode());
        try {
            toFix.update(todoDTO);
            todoRepository.update(toFix);
        }
        catch (DateTimeParseException e) {
            throw new IllegalArgumentException("날짜 형식을 다시 맞춰주세요! -> 년.월.일/시:분:초");
        }
    }
    public void DeleteTodo(Long code) {
        todoRepository.delete(code);
    }
    public Todo findByCode(Long code) {
       return todoRepository.findByCode(code);
    }
    public List<Long> getCodesByEmail(String email) {
        return todoRepository.findCodesByEmail(email);
    }

    //공백 예외처리
    private void checkCreateNull(TodoDTO todoDTO) {
        if(todoDTO.getDescription().isEmpty()) {
            throw new NullPointerException("설명을 입력해주세요!");
        }

        if(todoDTO.getEmail().isEmpty()) {
            throw new NullPointerException("이메일을 입력해주세요!");
        }
    }
    private void checkUpdateNull(TodoDTO todoDTO) {
        if(todoDTO.getDescription().isEmpty()) {
            throw new NullPointerException("설명을 입력해주세요!");
        }
        if(todoDTO.getEmail().isEmpty()) {
            throw new NullPointerException("이메일을 입력해주세요!");
        }
        if(todoDTO.getStartDate().isEmpty()) {
            throw new NullPointerException("시작일을 입력해주세요!");
        }
        if(todoDTO.getEndDate().isEmpty()) {
            throw new NullPointerException("마감일을 입력해주세요!");
        }
        if(Objects.isNull(todoDTO.getCode())) {
            throw new NullPointerException("작업 코드를 입력해주세요!");
        }

    }

}
