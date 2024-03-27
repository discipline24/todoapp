package SJToyProject.ToDoApp.Todo.repository;

import SJToyProject.ToDoApp.Todo.domain.Todo;

import java.util.List;

public interface TodoRepository {
    Todo findByCode(Long code);
    List<Todo> findAll();
    List<Long> findCodesByEmail(String email);

    Long create(Todo todo);
    Todo update(Todo todo);
    void delete(Long code);



}
