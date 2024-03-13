package dev.luisjohann.checklist.application.todo;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Service;

import dev.luisjohann.checklist.application.todo.dto.UncheckTodoDto;
import dev.luisjohann.checklist.domain.project.IWorkerRepository;
import dev.luisjohann.checklist.domain.project.Worker;
import dev.luisjohann.checklist.domain.project.exceptions.WorkerRequiredException;
import dev.luisjohann.checklist.domain.project.exceptions.WorkerWithSlugNotFoundException;
import dev.luisjohann.checklist.domain.todo.ITodoRepository;
import dev.luisjohann.checklist.domain.todo.Todo;
import dev.luisjohann.checklist.domain.todo.exception.TodoNotFoundException;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class UncheckTodoService {

    final IWorkerRepository workerRepository;
    final ITodoRepository todoRepository;

    public Mono<Todo> uncheckTodo(UncheckTodoDto dto) {
        try {
            Todo existingTodo = todoRepository.findByIdAndProjectSlug(dto.id(), dto.projectSlug()).toFuture().get();
            if (Objects.isNull(existingTodo)) {
                throw new TodoNotFoundException(dto.id(), dto.projectSlug());
            }

            Worker workerUncheck = null;
            if (Strings.isNotBlank(dto.uncheckWorkerSlug())) {
                workerUncheck = workerRepository
                        .findBySlugAndProjectSlug(dto.uncheckWorkerSlug(), dto.projectSlug())
                        .toFuture().get();
                if (Objects.isNull(workerUncheck)) {
                    throw new WorkerWithSlugNotFoundException(dto.uncheckWorkerSlug(), dto.projectSlug());
                }
            } else {
                throw new WorkerRequiredException();
            }

            Todo toUpdateTodo = buildTodo(existingTodo, dto);
            return todoRepository.updateTodo(toUpdateTodo);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return Mono.error(new RuntimeException());
    }

    private Todo buildTodo(Todo existingTodo, UncheckTodoDto dto) {
        return new Todo(existingTodo.getId(), existingTodo.getTitle(), existingTodo.getDescription(),
                existingTodo.getProject(), existingTodo.getAssignedTo(), existingTodo.getCreatedAt(),
                LocalDateTime.now(), null, null);
    }
}
