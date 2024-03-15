package dev.luisjohann.checklist.application.todo;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Service;

import dev.luisjohann.checklist.application.todo.dto.AddWorkerToTodoDto;
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
public class AddWorkerToTodoService {

    final IWorkerRepository workerRepository;
    final ITodoRepository todoRepository;

    public Mono<Todo> addWorkerToTodo(AddWorkerToTodoDto dto) {
        try {
            Todo existingTodo = todoRepository.findByIdAndProjectSlug(UUID.fromString(dto.todoId()), dto.projectSlug())
                    .toFuture().get();
            if (Objects.isNull(existingTodo)) {
                throw new TodoNotFoundException(dto.todoId(), dto.projectSlug());
            }

            Worker assignedWorker = null;
            if (Strings.isNotBlank(dto.workerSlug())) {
                assignedWorker = workerRepository
                        .findBySlugAndProjectSlug(dto.workerSlug(), dto.projectSlug())
                        .toFuture().get();
                if (Objects.isNull(assignedWorker)) {
                    throw new WorkerWithSlugNotFoundException(dto.workerSlug(), dto.projectSlug());
                }
            } else {
                throw new WorkerRequiredException();
            }

            Todo toUpdateTodo = buildTodo(existingTodo, assignedWorker);
            return todoRepository.updateTodo(toUpdateTodo);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return Mono.error(new RuntimeException());
    }

    private Todo buildTodo(Todo existingTodo, Worker assignedWorker) {
        return new Todo(existingTodo.id(), existingTodo.title(), existingTodo.description(),
                existingTodo.project(), assignedWorker, existingTodo.createdAt(),
                LocalDateTime.now(), existingTodo.checkedAt(), existingTodo.checkedWorker());
    }
}
