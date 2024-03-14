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

import dev.luisjohann.checklist.application.todo.UncheckTodoService;
import dev.luisjohann.checklist.application.todo.dto.UncheckTodoDto;
import dev.luisjohann.checklist.domain.project.Worker;
import dev.luisjohann.checklist.domain.todo.Todo;
import dev.luisjohann.checklist.infra.todo.controller.request.UncheckTodoRequest;
import dev.luisjohann.checklist.infra.todo.controller.response.UncheckTodoResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/todo-uncheck")
@RequiredArgsConstructor
@Slf4j
public class UncheckTodoController {

        final UncheckTodoService service;

        final BiFunction<Worker, Function<Worker, String>, String> workerCheck = (worker, getMethod) -> worker == null
                        ? null
                        : getMethod.apply(worker);

        @Operation(summary = "Unchecks a project TODO", tags = { "TODO" })
        @PatchMapping("/{project-slug}/{id}")
        @ResponseStatus(HttpStatus.OK)
        public Mono<ResponseEntity<UncheckTodoResponse>> uncheckTodo(@PathVariable("project-slug") String projectSlug,
                        @PathVariable("id") String todoId,
                        @RequestBody UncheckTodoRequest request) {
                log.info("CHECK TODO ID={} PROJECT_ID={} BY WORKER{}!", todoId, projectSlug,
                                request.uncheckWorkerSlug());

                return service.uncheckTodo(
                                new UncheckTodoDto(todoId, projectSlug, request.uncheckWorkerSlug()))
                                .flatMap(this::convertFlatMap);
        }

        private Mono<ResponseEntity<UncheckTodoResponse>> convertFlatMap(Todo todo) {
                return Mono.just(
                                ResponseEntity.ok()
                                                .body(new UncheckTodoResponse(todo.id(), todo.title(),
                                                                todo.description(),
                                                                workerCheck.apply(todo.assignedTo(),
                                                                                Worker::slug),
                                                                workerCheck.apply(todo.assignedTo(),
                                                                                Worker::name),
                                                                todo.createdAt(), todo.updatedAt(),
                                                                todo.checkedAt(),
                                                                workerCheck.apply(todo.checkedWorker(),
                                                                                Worker::slug),
                                                                workerCheck.apply(todo.checkedWorker(),
                                                                                Worker::name))));
        }

}