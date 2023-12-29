package dev.luisjohann.checklist.infra.todo;

import java.util.ArrayList;
import java.util.List;

import dev.luisjohann.checklist.domain.todo.ITodoRepository;
import dev.luisjohann.checklist.domain.todo.Todo;
import dev.luisjohann.checklist.domain.todo.exception.TodoNotFoundException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class TodoRepositoryInMemory implements ITodoRepository {

    private List<Todo> todos = new ArrayList<>();

    @Override
    public Mono<Todo> createTodo(Todo todo) {
        this.todos.add(todo);
        return Mono.just(todo);
    }

    @Override
    public Mono<Todo> updateTodo(Todo todo) {
        var index = this.todos.indexOf(todo);
        if (index < 0) {
            return Mono.error(new TodoNotFoundException(todo.getId(), todo.getProject().getSlug()));
        }
        this.todos.set(index, todo);
        return Mono.just(todo);
    }

    @Override
    public Mono<Void> removeTodo(Todo todo) {
        this.todos.removeIf(w -> w.getId().equals(todo.getId())
                && w.getProject().getSlug().equals(todo.getProject().getSlug()));
        return Mono.empty();
    }

    @Override
    public Flux<Todo> listAllTodosByProjectSlug(String projectSlug) {
        return Flux.fromIterable(
                this.todos.stream().filter(w -> w.getProject().getSlug().equals(projectSlug)).toList());
    }

}
