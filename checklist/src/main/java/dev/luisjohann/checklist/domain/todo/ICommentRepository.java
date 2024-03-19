package dev.luisjohann.checklist.domain.todo;

import java.util.UUID;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ICommentRepository {

    Mono<Comment> createComment(Comment comment);

    Mono<Void> removeComment(Comment comment);

    Mono<Comment> updateComment(Comment comment);

    Mono<Comment> findByIdAndTodoIdAndProjectSlug(UUID id, UUID todoId, String projectSlug);

    Flux<Comment> listAllCommentsByTodoIdAndTodoProjectSlug(UUID todoId, String projectSlug);
}
