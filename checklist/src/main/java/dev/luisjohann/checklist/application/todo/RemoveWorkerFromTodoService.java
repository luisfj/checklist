package dev.luisjohann.checklist.application.todo;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

import org.springframework.stereotype.Service;

import dev.luisjohann.checklist.application.todo.dto.RemoveWorkerFromTodoDto;
import dev.luisjohann.checklist.domain.project.IWorkerRepository;
import dev.luisjohann.checklist.domain.todo.ITodoRepository;
import dev.luisjohann.checklist.domain.todo.Todo;
import dev.luisjohann.checklist.domain.todo.exception.TodoNotFoundException;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class RemoveWorkerFromTodoService {

    final IWorkerRepository workerRepository;
    final ITodoRepository todoRepository;

    public Mono<Todo> removeWorkerFromTodo(RemoveWorkerFromTodoDto dto) {
        try {
            Todo existingTodo = todoRepository.findByIdAndProjectSlug(dto.todoId(), dto.projectSlug()).toFuture().get();
            if (Objects.isNull(existingTodo)) {
                throw new TodoNotFoundException(dto.todoId(), dto.projectSlug());
            }

            Todo toUpdateTodo = buildTodo(existingTodo);
            return todoRepository.updateTodo(toUpdateTodo);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return Mono.error(new RuntimeException());
    }

    private Todo buildTodo(Todo existingTodo) {
        return new Todo(existingTodo.getId(), existingTodo.getTitle(), existingTodo.getDescription(),
                existingTodo.getProject(), null, existingTodo.getCreatedAt(),
                LocalDateTime.now(), existingTodo.getCheckedAt(), existingTodo.getCheckedWorker());
    }
}
