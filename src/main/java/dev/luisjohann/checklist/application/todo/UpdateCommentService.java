package dev.luisjohann.checklist.application.todo;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Service;

import dev.luisjohann.checklist.application.todo.dto.UpdateCommentDto;
import dev.luisjohann.checklist.domain.project.IWorkerRepository;
import dev.luisjohann.checklist.domain.project.Worker;
import dev.luisjohann.checklist.domain.project.exceptions.WorkerRequiredException;
import dev.luisjohann.checklist.domain.project.exceptions.WorkerWithSlugNotFoundException;
import dev.luisjohann.checklist.domain.todo.Comment;
import dev.luisjohann.checklist.domain.todo.ICommentRepository;
import dev.luisjohann.checklist.domain.todo.ITodoRepository;
import dev.luisjohann.checklist.domain.todo.exception.CommentNotFoundException;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class UpdateCommentService {

    final IWorkerRepository workerRepository;
    final ITodoRepository todoRepository;
    final ICommentRepository commentRepository;

    public Mono<Comment> updateComment(UpdateCommentDto dto) {
        try {
            Comment comment = commentRepository
                    .findByIdAndTodoIdAndProjectSlug(dto.id(), dto.todoId(), dto.projectSlug()).toFuture().get();

            if (Objects.isNull(comment)) {
                throw new CommentNotFoundException(dto.id(), dto.todoId());
            }

            Worker updatedWorker = null;
            if (Strings.isNotBlank(dto.workerSlug())) {
                updatedWorker = workerRepository
                        .findBySlugAndProjectSlug(dto.workerSlug(), dto.projectSlug())
                        .toFuture().get();
                if (Objects.isNull(updatedWorker)) {
                    throw new WorkerWithSlugNotFoundException(dto.workerSlug(), dto.projectSlug());
                }
            } else {
                throw new WorkerRequiredException();
            }

            Comment updateComment = buildComment(comment, updatedWorker, dto);
            return commentRepository.createComment(updateComment);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return Mono.error(new RuntimeException());
    }

    private Comment buildComment(Comment existingComment, Worker updatedWorker, UpdateCommentDto dto) {
        return new Comment(existingComment.id(), existingComment.todo(), dto.comment(), existingComment.createdWorker(),
                existingComment.createdAt(), updatedWorker, LocalDateTime.now(), existingComment.deletedAt(),
                existingComment.deleteWorker());
    }
}
