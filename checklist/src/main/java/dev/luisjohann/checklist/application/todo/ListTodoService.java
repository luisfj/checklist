package dev.luisjohann.checklist.application.todo;

import org.springframework.stereotype.Service;

import dev.luisjohann.checklist.application.todo.dto.ListTodoDto;
import dev.luisjohann.checklist.domain.todo.ITodoRepository;
import dev.luisjohann.checklist.domain.todo.Todo;
import dev.luisjohann.checklist.domain.todo.exception.TodoNotFoundForTheProjectException;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;

@Service
@RequiredArgsConstructor
public class ListTodoService {

    final ITodoRepository todoRepository;

    public Flux<Todo> listTodo(ListTodoDto dto) {
        return todoRepository.listAllTodosByProjectSlug(dto.projectSlug())
                .switchIfEmpty(Flux.error(new TodoNotFoundForTheProjectException(dto.projectSlug())));
    }

}
