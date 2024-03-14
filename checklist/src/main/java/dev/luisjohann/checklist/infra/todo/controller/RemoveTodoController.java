package dev.luisjohann.checklist.infra.todo.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import dev.luisjohann.checklist.application.todo.RemoveTodoService;
import dev.luisjohann.checklist.application.todo.dto.RemoveTodoDto;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/todo")
@RequiredArgsConstructor
@Slf4j
public class RemoveTodoController {

        final RemoveTodoService service;

        @Operation(summary = "Deletes a TODO", tags = { "TODO" })
        @DeleteMapping("/{project-slug}/{id}")
        @ResponseStatus(HttpStatus.OK)
        public Mono<ResponseEntity<Void>> deleteTodo(@PathVariable("project-slug") String projectSlug,
                        @PathVariable("id") String todoId) {
                log.info("DELETE TODO ID={} PROJECT_ID={}!", todoId, projectSlug);

                return service.removeTodo(
                                new RemoveTodoDto(todoId, projectSlug))
                                .flatMap(v -> Mono.just(ResponseEntity.ok().build()));
        }
}