package dev.luisjohann.checklist.infra.todo.controller;

import java.util.function.BiFunction;
import java.util.function.Function;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import dev.luisjohann.checklist.application.todo.ListTodoCommentService;
import dev.luisjohann.checklist.application.todo.dto.ListTodoCommentsDto;
import dev.luisjohann.checklist.domain.project.Worker;
import dev.luisjohann.checklist.domain.todo.Comment;
import dev.luisjohann.checklist.infra.todo.controller.response.ListTodoCommentsResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/comment")
@RequiredArgsConstructor
@Slf4j
public class ListTodoCommentsController {

        final ListTodoCommentService service;

        final BiFunction<Worker, Function<Worker, String>, String> workerCheck = (worker, getMethod) -> worker == null
                        ? null
                        : getMethod.apply(worker);

        @Operation(summary = "Retrieves all TODO comments", tags = { "Comments" })
        @GetMapping("/{project-slug}/{todo-id}")
        @ResponseStatus(HttpStatus.OK)
        public Flux<ListTodoCommentsResponse> listTodo(@PathVariable("project-slug") String projectSlug,
                        @PathVariable("todo-id") String todoId) {
                log.info("LIST ALL COMMENTS FROM TODO SLUG={} AND TODO ID={}", projectSlug, todoId);

                return service.listTodoComments(new ListTodoCommentsDto(projectSlug, todoId))
                                .map(todo -> convertResponse(todo))
                                .flatMap(Flux::just);
        }

        private ListTodoCommentsResponse convertResponse(Comment comment) {
                return new ListTodoCommentsResponse(comment.id().toString(), comment.comment(),
                                comment.todo().id().toString(),
                                comment.todo().title(),
                                comment.todo().description(),
                                workerCheck.apply(comment.createdWorker(),
                                                Worker::slug),
                                workerCheck.apply(comment.createdWorker(),
                                                Worker::name),
                                workerCheck.apply(comment.deletedWorker(),
                                                Worker::slug),
                                workerCheck.apply(comment.deletedWorker(),
                                                Worker::name),
                                comment.createdAt(), comment.updatedAt(),
                                comment.deletedAt());
        }

}