package dev.luisjohann.checklist.domain.todo;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ITodoRepository {

    Mono<Todo> createTodo(Todo todo);

    Mono<Todo> updateTodo(Todo todo);

    Mono<Void> removeTodo(Todo todo);

    Flux<Todo> listAllTodosByProjectSlug(String projectSlug);
}
