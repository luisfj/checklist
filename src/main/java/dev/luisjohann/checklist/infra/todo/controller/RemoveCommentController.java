package dev.luisjohann.checklist.infra.todo.controller;

import java.util.function.BiFunction;
import java.util.function.Function;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.luisjohann.checklist.application.todo.RemoveCommentService;
import dev.luisjohann.checklist.application.todo.dto.RemoveCommentDto;
import dev.luisjohann.checklist.domain.project.Worker;
import dev.luisjohann.checklist.domain.todo.Comment;
import dev.luisjohann.checklist.infra.todo.controller.request.RemoveCommentRequest;
import dev.luisjohann.checklist.infra.todo.controller.response.RemoveCommentResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/comment")
@RequiredArgsConstructor
@Slf4j
public class RemoveCommentController {

        final RemoveCommentService service;

        @PatchMapping("/{project-slug}/{todo-id}/{id}")
        public Mono<ResponseEntity<RemoveCommentResponse>> removeComment(
                        @PathVariable("project-slug") String projectSlug,
                        @PathVariable("todo-id") String todoId,
                        @PathVariable("id") String id,
                        @RequestBody RemoveCommentRequest request) {
                log.info("REMOVE TODO COMMENT!");

                return service.removeComment(
                                new RemoveCommentDto(id, todoId, request.workerSlug(), projectSlug))
                                .flatMap(this::convertFlatMap);
        }

        private Mono<ResponseEntity<RemoveCommentResponse>> convertFlatMap(Comment comment) {
                BiFunction<Worker, Function<Worker, String>, String> workerCheck = (worker, getMethod) -> worker == null
                                ? null
                                : getMethod.apply(worker);

                return Mono.just(
                                ResponseEntity.ok()
                                                .body(new RemoveCommentResponse(comment.id(), comment.comment(),
                                                                comment.todo().getId(),
                                                                comment.todo().getTitle(),
                                                                comment.todo().getDescription(),
                                                                workerCheck.apply(comment.createdWorker(),
                                                                                Worker::getSlug),
                                                                workerCheck.apply(comment.createdWorker(),
                                                                                Worker::getName),
                                                                workerCheck.apply(comment.updatedWorker(),
                                                                                Worker::getSlug),
                                                                workerCheck.apply(comment.updatedWorker(),
                                                                                Worker::getName),
                                                                workerCheck.apply(comment.deleteWorker(),
                                                                                Worker::getSlug),
                                                                workerCheck.apply(comment.deleteWorker(),
                                                                                Worker::getName),
                                                                comment.createdAt(), comment.updatedAt(),
                                                                comment.deletedAt())));
        }
}