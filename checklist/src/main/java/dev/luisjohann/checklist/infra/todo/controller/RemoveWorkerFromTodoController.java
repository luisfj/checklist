package dev.luisjohann.checklist.infra.todo.controller;

import java.util.function.BiFunction;
import java.util.function.Function;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import dev.luisjohann.checklist.application.todo.RemoveWorkerFromTodoService;
import dev.luisjohann.checklist.application.todo.dto.RemoveWorkerFromTodoDto;
import dev.luisjohann.checklist.domain.project.Worker;
import dev.luisjohann.checklist.domain.todo.Todo;
import dev.luisjohann.checklist.infra.todo.controller.response.RemoveWorkerFromTodoResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/todo-worker-remove")
@RequiredArgsConstructor
@Slf4j
public class RemoveWorkerFromTodoController {

        final RemoveWorkerFromTodoService service;

        final BiFunction<Worker, Function<Worker, String>, String> workerCheck = (worker, getMethod) -> worker == null
                        ? null
                        : getMethod.apply(worker);

        @Operation(summary = "Removes a TODO worker", tags = { "TODO" })
        @PatchMapping("/{project-slug}/{id}")
        @ResponseStatus(HttpStatus.OK)
        public Mono<ResponseEntity<RemoveWorkerFromTodoResponse>> removeWorkerFromTodo(
                        @PathVariable("project-slug") String projectSlug,
                        @PathVariable("id") String todoId) {
                log.info("REMOVE WORKER FROM TODO!");

                return service.removeWorkerFromTodo(
                                new RemoveWorkerFromTodoDto(todoId, projectSlug))
                                .flatMap(this::convertFlatMap);
        }

        private Mono<ResponseEntity<RemoveWorkerFromTodoResponse>> convertFlatMap(Todo todo) {
                return Mono.just(
                                ResponseEntity.ok()
                                                .body(new RemoveWorkerFromTodoResponse(todo.id().toString(),
                                                                todo.title(),
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
