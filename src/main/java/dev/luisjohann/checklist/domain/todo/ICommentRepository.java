package dev.luisjohann.checklist.domain.todo;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ICommentRepository {

    Mono<Comment> createComment(Comment comment);

    Mono<Void> removeComment(Comment comment);

    Mono<Comment> updateComment(Comment comment);

    Mono<Comment> findByIdAndTodoIdAndProjectSlug(String id, String todoId, String projectSlug);

    Flux<Comment> listAllCommentsByTodoId(String todoId);
}
