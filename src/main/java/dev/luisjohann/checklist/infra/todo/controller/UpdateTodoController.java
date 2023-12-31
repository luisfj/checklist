package dev.luisjohann.checklist.infra.todo.controller;

import java.net.URI;
import java.util.function.BiFunction;
import java.util.function.Function;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.luisjohann.checklist.application.todo.UpdateTodoService;
import dev.luisjohann.checklist.application.todo.dto.UpdateTodoDto;
import dev.luisjohann.checklist.domain.project.Worker;
import dev.luisjohann.checklist.domain.todo.Todo;
import dev.luisjohann.checklist.infra.todo.controller.request.UpdateTodoRequest;
import dev.luisjohann.checklist.infra.todo.controller.response.UpdateTodoResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/todo")
@RequiredArgsConstructor
@Slf4j
public class UpdateTodoController {

        final UpdateTodoService service;

        @PutMapping("/{project-slug}/{id}")
        public Mono<ResponseEntity<UpdateTodoResponse>> createTodo(@PathVariable("project-slug") String projectSlug,
                        @PathVariable("id") String todoId,
                        @RequestBody UpdateTodoRequest request) {
                log.info("UPDATE TODO ID={} PROJECT_ID={}!", todoId, projectSlug);

                return service.updateTodo(
                                new UpdateTodoDto(todoId, request.title(), request.description(), projectSlug,
                                                request.workerAssignedSlug()))
                                .flatMap(this::convertFlatMap);
        }

        private Mono<ResponseEntity<UpdateTodoResponse>> convertFlatMap(Todo todo) {
                BiFunction<Worker, Function<Worker, String>, String> workerCheck = (worker, getMethod) -> worker == null
                                ? null
                                : getMethod.apply(worker);

                return Mono.just(
                                ResponseEntity.created(URI.create(todo.getId()))
                                                .body(new UpdateTodoResponse(todo.getId(), todo.getTitle(),
                                                                todo.getDescription(),
                                                                workerCheck.apply(todo.getAssignedTo(),
                                                                                Worker::getSlug),
                                                                workerCheck.apply(todo.getAssignedTo(),
                                                                                Worker::getName))));
        }
}