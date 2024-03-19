package dev.luisjohann.checklist.infra.todo.repository.jpa;

import java.util.Objects;
import java.util.UUID;
import java.util.function.Function;
import java.util.function.Supplier;

import org.springframework.data.repository.NoRepositoryBean;

import dev.luisjohann.checklist.domain.todo.Comment;
import dev.luisjohann.checklist.domain.todo.ICommentRepository;
import dev.luisjohann.checklist.domain.todo.exception.CommentNotFoundException;
import dev.luisjohann.checklist.infra.jpa.model.util.ConverterJpaUtil;
import dev.luisjohann.checklist.infra.todo.repository.jpa.model.CommentJpaModel;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@NoRepositoryBean
@RequiredArgsConstructor
public class CommentRepositoryJpaImpl implements ICommentRepository {

    final CommentRepositoryJpa repositoryJpa;

    @Override
    public Mono<Comment> createComment(Comment comment) {
        var cComment = ConverterJpaUtil.convertFromRecord(comment);
        var savedComment = repositoryJpa.save(cComment);
        return findByIdAndTodoIdAndProjectSlug(savedComment.getId(), comment.todo().id(),
                comment.todo().project().slug());
    }

    @Override
    public Mono<Void> removeComment(Comment comment) {
        return verifyCommentExists(comment,
                (bdComment) -> {
                    bdComment.setDeletedAt(comment.deletedAt());
                    bdComment.setDeletedWorker(ConverterJpaUtil.convertFromRecord(comment.deletedWorker()));
                    this.repositoryJpa.save(bdComment);
                    return Mono.empty();
                },
                () -> Mono
                        .error(new CommentNotFoundException(comment.id().toString(), comment.todo().id().toString())));
    }

    @Override
    public Mono<Comment> updateComment(Comment comment) {
        return verifyCommentExists(comment,
                (bdComment) -> {
                    bdComment.setComment(comment.comment());
                    bdComment.setDeletedAt(comment.deletedAt());
                    bdComment.setDeletedWorker(ConverterJpaUtil.convertFromRecord(comment.deletedWorker()));
                    bdComment.setUpdatedAt(comment.updatedAt());
                    bdComment.setUpdatedWorker(ConverterJpaUtil.convertFromRecord(comment.updatedWorker()));

                    this.repositoryJpa.save(bdComment);
                    return Mono.just(comment);
                },
                () -> Mono
                        .error(new CommentNotFoundException(comment.id().toString(), comment.todo().id().toString())));
    }

    @Override
    public Mono<Comment> findByIdAndTodoIdAndProjectSlug(UUID id, UUID todoId, String projectSlug) {
        return verifyCommentExists(id, todoId, projectSlug,
                (commentModel) -> Mono.just(ConverterJpaUtil.convertToRecord(commentModel)), Mono::empty);
    }

    @Override
    public Flux<Comment> listAllCommentsByTodoIdAndTodoProjectSlug(UUID todoId, String projectSlug) {
        return Flux.fromIterable(
                this.repositoryJpa.findAllByTodoIdAndTodoProjectSlugAndDeletedAtIsNull(todoId, projectSlug).stream()
                        .map(ConverterJpaUtil::convertToRecord)
                        .toList());
    }

    private <T> Mono<T> verifyCommentExists(Comment comment, Function<CommentJpaModel, Mono<T>> exists,
            Supplier<Mono<T>> notExists) {
        if (Objects.isNull(comment)) {
            throw new CommentNotFoundException(null, null);
        }

        return verifyCommentExists(comment.id(), comment.todo().id(), comment.todo().project().slug(), exists,
                notExists);
    }

    private <T> Mono<T> verifyCommentExists(UUID id, UUID todoId, String projectSlug,
            Function<CommentJpaModel, Mono<T>> exists,
            Supplier<Mono<T>> notExists) {
        var todo = this.repositoryJpa.findByIdAndTodoIdAndTodoProjectSlugAndDeletedAtIsNull(id, todoId, projectSlug);
        if (!todo.isPresent()) {
            return notExists.get();
        }
        return exists.apply(todo.get());
    }
}
