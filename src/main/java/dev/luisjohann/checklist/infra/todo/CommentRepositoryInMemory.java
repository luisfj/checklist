package dev.luisjohann.checklist.infra.todo;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import dev.luisjohann.checklist.domain.todo.Comment;
import dev.luisjohann.checklist.domain.todo.ICommentRepository;
import dev.luisjohann.checklist.domain.todo.exception.CommentNotFoundException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class CommentRepositoryInMemory implements ICommentRepository {

    private List<Comment> comments = new ArrayList<>();

    @Override
    public Mono<Comment> createComment(Comment comment) {
        this.comments.add(comment);
        return Mono.just(comment);
    }

    @Override
    public Mono<Comment> updateComment(Comment comment) {
        var idx = this.comments.indexOf(comment);

        if (idx < 0 || this.comments.get(idx).todo().equals(comment.todo())) {
            return Mono.error(new CommentNotFoundException(comment.id(), comment.todo().getId()));
        }

        this.comments.set(idx, comment);

        return Mono.just(comment);
    }

    @Override
    public Mono<Comment> findByIdAndTodoIdAndProjectSlug(String id, String todoId, String projectSlug) {
        var comment = this.comments.stream()
                .filter(c -> c.id().equals(id) && c.todo().getId().equals(todoId)
                        && c.todo().getProject().getSlug().equals(projectSlug))
                .findFirst()
                .orElse(null);
        return Objects.isNull(comment) ? Mono.empty() : Mono.just(comment);
    }

    @Override
    public Flux<Comment> listAllCommentsByTodoId(String todoId) {
        return Flux.fromIterable(
                this.comments.stream().filter(c -> c.todo().getId().equals(todoId)).collect(Collectors.toList()));
    }

    @Override
    public Mono<Void> removeComment(Comment comment) {
        this.comments.removeIf(c -> c.id().equals(comment.id())
                && c.todo().getId().equals(comment.todo().getId()));
        return Mono.empty();
    }

}
