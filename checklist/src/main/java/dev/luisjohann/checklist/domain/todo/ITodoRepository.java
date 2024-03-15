package dev.luisjohann.checklist.domain.todo;

import java.util.UUID;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ITodoRepository {

    Mono<Todo> createTodo(Todo todo);

    Mono<Todo> updateTodo(Todo todo);

    Mono<Void> removeTodo(Todo todo);

    Mono<Todo> findByIdAndProjectSlug(UUID id, String projectSlug);

    Flux<Todo> listAllTodosByProjectSlug(String projectSlug);
}
