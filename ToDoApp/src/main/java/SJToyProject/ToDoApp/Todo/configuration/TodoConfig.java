package SJToyProject.ToDoApp.Todo.configuration;

import SJToyProject.ToDoApp.Todo.repository.MySqlTodoRepository;
import SJToyProject.ToDoApp.Todo.repository.TodoRepository;
import SJToyProject.ToDoApp.Todo.service.TodoService;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TodoConfig {
    public final EntityManager em;

    @Autowired
    public TodoConfig(EntityManager em) {
        this.em = em;
    }

    @Bean
    public TodoService todoService() {
        return new TodoService(todoRepository());
    }

    @Bean
    public TodoRepository todoRepository() {
        return new MySqlTodoRepository(em);
    }

}
