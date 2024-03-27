package SJToyProject.ToDoApp.Todo.repository;

import SJToyProject.ToDoApp.Todo.domain.Todo;
import jakarta.persistence.EntityManager;

import java.util.List;

public class MySqlTodoRepository implements TodoRepository {
    private final EntityManager em;
    public MySqlTodoRepository(EntityManager em) {
        this.em = em;
    }

    @Override
    public Todo findByCode(Long code) {
        Todo todo = em.find(Todo.class, code);
        if(todo == null) {
            throw new NullPointerException("해당 코드가 존재하지 않습니다!");
        }
        return todo;
    }

    @Override
    public List<Todo> findAll() {
        return em.createQuery("select a from Todo a", Todo.class).getResultList();
    }
    @Override
    public List<Long> findCodesByEmail(String email) {
        return em.createQuery("SELECT t.code FROM Todo t WHERE t.email = :email", Long.class)
                .setParameter("email", email)
                .getResultList();
    }
    @Override
    public Long create(Todo todo) {
        em.persist(todo);
        return todo.getCode();
    }

    @Override
    public Todo update(Todo todo) {
        em.merge(todo);
        return todo;
    }
    @Override
    public void delete(Long code) {
        em.remove(this.findByCode(code));
    }
}
