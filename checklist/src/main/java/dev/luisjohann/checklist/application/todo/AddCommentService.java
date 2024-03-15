package dev.luisjohann.checklist.application.todo;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Service;

import dev.luisjohann.checklist.application.todo.dto.AddCommentDto;
import dev.luisjohann.checklist.domain.project.IWorkerRepository;
import dev.luisjohann.checklist.domain.project.Worker;
import dev.luisjohann.checklist.domain.project.exceptions.WorkerRequiredException;
import dev.luisjohann.checklist.domain.project.exceptions.WorkerWithSlugNotFoundException;
import dev.luisjohann.checklist.domain.todo.Comment;
import dev.luisjohann.checklist.domain.todo.ICommentRepository;
import dev.luisjohann.checklist.domain.todo.ITodoRepository;
import dev.luisjohann.checklist.domain.todo.Todo;
import dev.luisjohann.checklist.domain.todo.exception.TodoNotFoundException;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class AddCommentService {

    final IWorkerRepository workerRepository;
    final ITodoRepository todoRepository;
    final ICommentRepository commentRepository;

    public Mono<Comment> addComment(AddCommentDto dto) {
        try {
            Todo existingTodo = todoRepository.findByIdAndProjectSlug(UUID.fromString(dto.todoId()), dto.projectSlug())
                    .toFuture().get();
            if (Objects.isNull(existingTodo)) {
                throw new TodoNotFoundException(dto.todoId(), dto.projectSlug());
            }

            Worker createdWorker = null;
            if (Strings.isNotBlank(dto.workerSlug())) {
                createdWorker = workerRepository
                        .findBySlugAndProjectSlug(dto.workerSlug(), dto.projectSlug())
                        .toFuture().get();
                if (Objects.isNull(createdWorker)) {
                    throw new WorkerWithSlugNotFoundException(dto.workerSlug(), dto.projectSlug());
                }
            } else {
                throw new WorkerRequiredException();
            }

            Comment createComment = buildComment(existingTodo, createdWorker, dto);
            return commentRepository.createComment(createComment);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return Mono.error(new RuntimeException());
    }

    private Comment buildComment(Todo existingTodo, Worker createdWorker, AddCommentDto dto) {
        return new Comment(UUID.randomUUID().toString(), existingTodo, dto.comment(), createdWorker,
                LocalDateTime.now(), null, null, null, null);
    }
}
