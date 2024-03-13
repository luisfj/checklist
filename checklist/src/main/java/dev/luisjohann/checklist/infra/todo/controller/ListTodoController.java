package dev.luisjohann.checklist.infra.todo.controller;

import java.util.function.BiFunction;
import java.util.function.Function;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import dev.luisjohann.checklist.application.todo.ListTodoService;
import dev.luisjohann.checklist.application.todo.dto.ListTodoDto;
import dev.luisjohann.checklist.domain.project.Worker;
import dev.luisjohann.checklist.domain.todo.Todo;
import dev.luisjohann.checklist.infra.todo.controller.response.ListTodoResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/todo")
@RequiredArgsConstructor
@Slf4j
public class ListTodoController {

        final ListTodoService service;

        final BiFunction<Worker, Function<Worker, String>, String> workerCheck = (worker, getMethod) -> worker == null
                        ? null
                        : getMethod.apply(worker);

        @GetMapping("/{project-slug}")
        @ResponseStatus(HttpStatus.OK)
        public Flux<ListTodoResponse> listTodo(@PathVariable("project-slug") String projectSlug) {
                log.info("LIST ALL TODO FROM PROJECT SLUG={}", projectSlug);

                return service.listTodo(new ListTodoDto(projectSlug))
                                .map(todo -> convertResponse(todo))
                                .flatMap(Flux::just);
        }

        private ListTodoResponse convertResponse(Todo todo) {
                return new ListTodoResponse(todo.getId(), todo.getTitle(), todo.getDescription(),
                                workerCheck.apply(todo.getAssignedTo(), Worker::getSlug),
                                workerCheck.apply(todo.getAssignedTo(), Worker::getName),
                                todo.getCreatedAt(), todo.getUpdatedAt(), todo.getCheckedAt(),
                                workerCheck.apply(todo.getCheckedWorker(), Worker::getSlug),
                                workerCheck.apply(todo.getCheckedWorker(), Worker::getName));
        }

}