package dev.luisjohann.checklist.domain.todo;

import java.io.Serializable;
import java.time.LocalDateTime;

import org.apache.commons.lang3.StringUtils;

import dev.luisjohann.checklist.domain.project.Worker;
import dev.luisjohann.checklist.domain.todo.exception.CreatedWorkerRequiredException;
import dev.luisjohann.checklist.domain.todo.exception.EmptyCommentException;
import dev.luisjohann.checklist.domain.todo.exception.TodoRequiredException;

public record Comment(String id, Todo todo, String comment, Worker createdWorker, LocalDateTime createdAt,
        Worker updatedWorker, LocalDateTime updatedAt, LocalDateTime deletedAt, Worker deleteWorker)
        implements Serializable {

    public Comment {
        if (todo == null) {
            throw new TodoRequiredException();
        }
        if (StringUtils.isBlank(comment)) {
            throw new EmptyCommentException();
        }
        if (createdWorker == null) {
            throw new CreatedWorkerRequiredException();
        }
    }

}
