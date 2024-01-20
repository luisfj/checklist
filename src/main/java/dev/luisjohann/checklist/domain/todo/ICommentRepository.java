package dev.luisjohann.checklist.domain.todo;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ICommentRepository {

    Mono<Comment> createComment(Comment comment);

    Mono<Comment> updateComment(Comment comment);

    Mono<Comment> findByIdAndTodoId(String id, String todoId);

    Flux<Comment> listAllCommentsByTodoId(String todoId);
}
