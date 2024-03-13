package dev.luisjohann.checklist.application.todo;

import java.util.Objects;
import java.util.concurrent.ExecutionException;

import org.springframework.stereotype.Service;

import dev.luisjohann.checklist.application.todo.dto.RemoveTodoDto;
import dev.luisjohann.checklist.domain.todo.ITodoRepository;
import dev.luisjohann.checklist.domain.todo.Todo;
import dev.luisjohann.checklist.domain.todo.exception.TodoNotFoundException;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class RemoveTodoService {

    final ITodoRepository todoRepository;

    public Mono<Void> removeTodo(RemoveTodoDto dto) {
        try {
            Todo existingTodo = todoRepository.findByIdAndProjectSlug(dto.id(), dto.projectSlug()).toFuture().get();
            if (Objects.isNull(existingTodo)) {
                throw new TodoNotFoundException(dto.id(), dto.projectSlug());
            }
            return todoRepository.removeTodo(existingTodo);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return Mono.error(new RuntimeException());
    }
}
