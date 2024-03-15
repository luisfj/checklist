package dev.luisjohann.checklist.infra.todo.repository.memory;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Function;
import java.util.function.Supplier;

import dev.luisjohann.checklist.domain.todo.ITodoRepository;
import dev.luisjohann.checklist.domain.todo.Todo;
import dev.luisjohann.checklist.domain.todo.exception.TodoNotFoundException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class TodoRepositoryInMemory implements ITodoRepository {

    private Map<UUID, Todo> todos = new HashMap<>();

    @Override
    public Mono<Todo> createTodo(Todo todo) {
        var newTodo = new Todo(UUID.randomUUID(), todo.title(), todo.description(), todo.project(), todo.assignedTo(),
                todo.createdAt(), todo.updatedAt(), todo.checkedAt(), todo.checkedWorker());
        var add = this.todos.putIfAbsent(newTodo.id(), newTodo);
        return Mono.just(add == null ? newTodo : add);
    }

    @Override
    public Mono<Todo> updateTodo(Todo todo) {
        return verifyTodoExists(todo,
                (bdTodo) -> {
                    this.todos.replace(bdTodo.id(), todo);
                    return Mono.just(todo);
                },
                () -> Mono.error(new TodoNotFoundException(todo.id().toString(), todo.project().slug())));
    }

    @Override
    public Mono<Void> removeTodo(Todo todo) {
        return verifyTodoExists(todo,
                (bdTodo) -> {
                    this.todos.remove(bdTodo.id());
                    return Mono.empty();
                },
                () -> Mono.error(new TodoNotFoundException(todo.id().toString(), todo.project().slug())));
    }

    @Override
    public Mono<Todo> findByIdAndProjectSlug(UUID id, String projectSlug) {
        return verifyTodoExists(id, projectSlug, Mono::just, Mono::empty);
    }

    @Override
    public Flux<Todo> listAllTodosByProjectSlug(String projectSlug) {
        return Flux.fromIterable(
                this.todos.values().stream().filter(w -> w.project().slug().equals(projectSlug)).toList());
    }

    private <T> Mono<T> verifyTodoExists(Todo todo, Function<Todo, Mono<T>> exists,
            Supplier<Mono<T>> notExists) {
        if (Objects.isNull(todo)) {
            throw new TodoNotFoundException(null, null);
        }

        return verifyTodoExists(todo.id(), todo.project().slug(), exists, notExists);
    }

    private <T> Mono<T> verifyTodoExists(UUID id, String projectSlug, Function<Todo, Mono<T>> exists,
            Supplier<Mono<T>> notExists) {
        var todo = this.todos.get(id);
        if (todo == null || !todo.project().slug().equals(projectSlug)) {
            return notExists.get();
        }
        return exists.apply(todo);
    }
}
