package dev.luisjohann.checklist.application.todo;

import java.util.UUID;

import org.springframework.stereotype.Service;

import dev.luisjohann.checklist.application.todo.dto.ListTodoCommentsDto;
import dev.luisjohann.checklist.domain.todo.Comment;
import dev.luisjohann.checklist.domain.todo.ICommentRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;

@Service
@RequiredArgsConstructor
public class ListTodoCommentService {

    final ICommentRepository commentRepository;

    public Flux<Comment> listTodoComments(ListTodoCommentsDto dto) {
        return commentRepository
                .listAllCommentsByTodoIdAndTodoProjectSlug(UUID.fromString(dto.todoId()), dto.projectSlug())
                .switchIfEmpty(Flux.empty());
    }

}
