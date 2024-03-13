package dev.luisjohann.checklist.application.todo;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Service;

import dev.luisjohann.checklist.application.todo.dto.UpdateTodoDto;
import dev.luisjohann.checklist.domain.project.IWorkerRepository;
import dev.luisjohann.checklist.domain.project.Worker;
import dev.luisjohann.checklist.domain.project.exceptions.WorkerWithSlugNotFoundException;
import dev.luisjohann.checklist.domain.todo.ITodoRepository;
import dev.luisjohann.checklist.domain.todo.Todo;
import dev.luisjohann.checklist.domain.todo.exception.TodoNotFoundException;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class UpdateTodoService {

    final IWorkerRepository workerRepository;
    final ITodoRepository todoRepository;

    public Mono<Todo> updateTodo(UpdateTodoDto dto) {
        try {
            Todo existingTodo = todoRepository.findByIdAndProjectSlug(dto.id(), dto.projectSlug()).toFuture().get();
            if (Objects.isNull(existingTodo)) {
                throw new TodoNotFoundException(dto.id(), dto.projectSlug());
            }

            Worker workerAssigned = null;
            if (Strings.isNotBlank(dto.workerAssignedSlug())) {
                workerAssigned = workerRepository
                        .findBySlugAndProjectSlug(dto.workerAssignedSlug(), dto.projectSlug())
                        .toFuture().get();
                if (Objects.isNull(workerAssigned)) {
                    throw new WorkerWithSlugNotFoundException(dto.workerAssignedSlug(), dto.projectSlug());
                }
            }

            Todo toUpdateTodo = buildTodo(existingTodo, workerAssigned, dto);
            return todoRepository.updateTodo(toUpdateTodo);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return Mono.error(new RuntimeException());
    }

    private Todo buildTodo(Todo existingTodo, Worker workerAssignedTo, UpdateTodoDto dto) {
        return new Todo(existingTodo.id(), dto.title(), dto.description(), existingTodo.project(),
                workerAssignedTo,
                existingTodo.createdAt(), LocalDateTime.now(), null, null);
    }
}
