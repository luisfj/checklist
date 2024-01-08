package dev.luisjohann.checklist.infra.todo.controller;

import java.util.function.BiFunction;
import java.util.function.Function;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import dev.luisjohann.checklist.application.todo.CheckTodoService;
import dev.luisjohann.checklist.application.todo.dto.CheckTodoDto;
import dev.luisjohann.checklist.domain.project.Worker;
import dev.luisjohann.checklist.domain.todo.Todo;
import dev.luisjohann.checklist.infra.todo.controller.request.CheckTodoRequest;
import dev.luisjohann.checklist.infra.todo.controller.response.CheckTodoResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/todo-check")
@RequiredArgsConstructor
@Slf4j
public class CheckTodoController {

        final CheckTodoService service;

        final BiFunction<Worker, Function<Worker, String>, String> workerCheck = (worker, getMethod) -> worker == null
                        ? null
                        : getMethod.apply(worker);

        @PatchMapping("/{project-slug}/{id}")
        @ResponseStatus(HttpStatus.OK)
        public Mono<ResponseEntity<CheckTodoResponse>> checkTodo(@PathVariable("project-slug") String projectSlug,
                        @PathVariable("id") String todoId,
                        @RequestBody CheckTodoRequest request) {
                log.info("CHECK TODO ID={} PROJECT_ID={} BY WORKER{}!", todoId, projectSlug, request.checkWorkerSlug());

                return service.checkTodo(
                                new CheckTodoDto(todoId, projectSlug, request.checkWorkerSlug()))
                                .flatMap(this::convertFlatMap);
        }

        private Mono<ResponseEntity<CheckTodoResponse>> convertFlatMap(Todo todo) {
                return Mono.just(
                                ResponseEntity.ok()
                                                .body(new CheckTodoResponse(todo.getId(), todo.getTitle(),
                                                                todo.getDescription(),
                                                                workerCheck.apply(todo.getAssignedTo(),
                                                                                Worker::getSlug),
                                                                workerCheck.apply(todo.getAssignedTo(),
                                                                                Worker::getName),
                                                                todo.getCreatedAt(), todo.getUpdatedAt(),
                                                                todo.getCheckedAt(),
                                                                workerCheck.apply(todo.getCheckedWorker(),
                                                                                Worker::getSlug),
                                                                workerCheck.apply(todo.getCheckedWorker(),
                                                                                Worker::getName))));
        }

}