package dev.luisjohann.checklist.infra.todo.controller;

import java.util.function.BiFunction;
import java.util.function.Function;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.luisjohann.checklist.application.todo.UpdateCommentService;
import dev.luisjohann.checklist.application.todo.dto.UpdateCommentDto;
import dev.luisjohann.checklist.domain.project.Worker;
import dev.luisjohann.checklist.domain.todo.Comment;
import dev.luisjohann.checklist.infra.todo.controller.request.UpdateCommentRequest;
import dev.luisjohann.checklist.infra.todo.controller.response.UpdateCommentResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/comment")
@RequiredArgsConstructor
@Slf4j
public class UpdateCommentController {

        final UpdateCommentService service;

        @Operation(summary = "Updates a todo comment", tags = { "Comments" })
        @PutMapping("/{project-slug}/{todo-id}/{id}")
        public Mono<ResponseEntity<UpdateCommentResponse>> updateComment(
                        @PathVariable("project-slug") String projectSlug,
                        @PathVariable("todo-id") String todoId,
                        @PathVariable("id") String id,
                        @RequestBody UpdateCommentRequest request) {
                log.info("UPDATE COMMENT TODO!");

                return service.updateComment(
                                new UpdateCommentDto(id, request.comment(), todoId, request.workerSlug(), projectSlug))
                                .flatMap(this::convertFlatMap);
        }

        private Mono<ResponseEntity<UpdateCommentResponse>> convertFlatMap(Comment comment) {
                BiFunction<Worker, Function<Worker, String>, String> workerCheck = (worker, getMethod) -> worker == null
                                ? null
                                : getMethod.apply(worker);

                return Mono.just(
                                ResponseEntity.ok()
                                                .body(new UpdateCommentResponse(comment.id().toString(),
                                                                comment.comment(),
                                                                comment.todo().id().toString(),
                                                                comment.todo().title(),
                                                                comment.todo().description(),
                                                                workerCheck.apply(comment.createdWorker(),
                                                                                Worker::slug),
                                                                workerCheck.apply(comment.createdWorker(),
                                                                                Worker::name),
                                                                workerCheck.apply(comment.updatedWorker(),
                                                                                Worker::slug),
                                                                workerCheck.apply(comment.updatedWorker(),
                                                                                Worker::name),
                                                                workerCheck.apply(comment.deletedWorker(),
                                                                                Worker::slug),
                                                                workerCheck.apply(comment.deletedWorker(),
                                                                                Worker::name),
                                                                comment.createdAt(), comment.updatedAt(),
                                                                comment.deletedAt())));
        }
}