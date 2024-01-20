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

import dev.luisjohann.checklist.application.todo.AddCommentService;
import dev.luisjohann.checklist.application.todo.dto.AddCommentDto;
import dev.luisjohann.checklist.domain.project.Worker;
import dev.luisjohann.checklist.domain.todo.Comment;
import dev.luisjohann.checklist.infra.todo.controller.request.AddCommentRequest;
import dev.luisjohann.checklist.infra.todo.controller.response.AddCommentResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/comment")
@RequiredArgsConstructor
@Slf4j
public class AddCommentController {

        final AddCommentService service;

        @PostMapping("/{project-slug}/{todo-id}")
        public Mono<ResponseEntity<AddCommentResponse>> addComment(
                        @PathVariable("project-slug") String projectSlug,
                        @PathVariable("todo-id") String todoId,
                        @RequestBody AddCommentRequest request) {
                log.info("ADD COMMENT TO TODO!");

                return service.addComment(
                                new AddCommentDto(request.comment(), todoId, request.workerSlug(), projectSlug))
                                .flatMap(this::convertFlatMap);
        }

        private Mono<ResponseEntity<AddCommentResponse>> convertFlatMap(Comment comment) {
                BiFunction<Worker, Function<Worker, String>, String> workerCheck = (worker, getMethod) -> worker == null
                                ? null
                                : getMethod.apply(worker);

                return Mono.just(
                                ResponseEntity.created(URI.create(comment.id()))
                                                .body(new AddCommentResponse(comment.id(), comment.comment(),
                                                                comment.todo().getId(),
                                                                comment.todo().getTitle(),
                                                                comment.todo().getDescription(),
                                                                workerCheck.apply(comment.createdWorker(),
                                                                                Worker::getSlug),
                                                                workerCheck.apply(comment.createdWorker(),
                                                                                Worker::getName),
                                                                workerCheck.apply(comment.deleteWorker(),
                                                                                Worker::getSlug),
                                                                workerCheck.apply(comment.deleteWorker(),
                                                                                Worker::getName),
                                                                comment.createdAt(), comment.updatedAt(),
                                                                comment.deletedAt())));
        }
}