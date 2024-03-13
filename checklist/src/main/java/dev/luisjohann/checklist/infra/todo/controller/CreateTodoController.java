package dev.luisjohann.checklist.infra.todo.controller;

import java.net.URI;
import java.util.function.BiFunction;
import java.util.function.Function;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.luisjohann.checklist.application.todo.CreateTodoService;
import dev.luisjohann.checklist.application.todo.dto.CreateTodoDto;
import dev.luisjohann.checklist.domain.project.Worker;
import dev.luisjohann.checklist.domain.todo.Todo;
import dev.luisjohann.checklist.infra.todo.controller.request.CreateTodoRequest;
import dev.luisjohann.checklist.infra.todo.controller.response.CreateTodoResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/todo")
@RequiredArgsConstructor
@Slf4j
public class CreateTodoController {

        final CreateTodoService service;

        @PostMapping("/{project-slug}")
        public Mono<ResponseEntity<CreateTodoResponse>> createTodo(@PathVariable("project-slug") String projectSlug,
                        @RequestBody CreateTodoRequest request) {
                log.info("ADD TODO!");

                return service.createTodo(
                                new CreateTodoDto(request.title(), request.description(), projectSlug,
                                                request.workerAssignedSlug()))
                                .flatMap(this::convertFlatMap);
        }

        private Mono<ResponseEntity<CreateTodoResponse>> convertFlatMap(Todo todo) {
                BiFunction<Worker, Function<Worker, String>, String> workerCheck = (worker, getMethod) -> worker == null
                                ? null
                                : getMethod.apply(worker);

                return Mono.just(
                                ResponseEntity.created(URI.create(todo.getId()))
                                                .body(new CreateTodoResponse(todo.getId(), todo.getTitle(),
                                                                todo.getDescription(),
                                                                workerCheck.apply(todo.getAssignedTo(),
                                                                                Worker::getSlug),
                                                                workerCheck.apply(todo.getAssignedTo(),
                                                                                Worker::getName))));
        }
}