package dev.luisjohann.checklist.domain.todo;

import java.io.Serializable;
import java.time.LocalDateTime;

import org.apache.commons.lang3.StringUtils;

import dev.luisjohann.checklist.domain.project.Project;
import dev.luisjohann.checklist.domain.project.Worker;
import dev.luisjohann.checklist.domain.project.exceptions.ProjectRequiredException;
import dev.luisjohann.checklist.domain.todo.exception.EmptyTodoDescriptionException;
import dev.luisjohann.checklist.domain.todo.exception.EmptyTodoTitleException;

public record Todo(String id, String title, String description, Project project, Worker assignedTo,
        LocalDateTime createdAt, LocalDateTime updatedAt, LocalDateTime checkedAt, Worker checkedWorker)
        implements Serializable {

    public Todo {
        if (StringUtils.isBlank(title)) {
            throw new EmptyTodoTitleException();
        }
        if (StringUtils.isBlank(description)) {
            throw new EmptyTodoDescriptionException();
        }
        if (project == null) {
            throw new ProjectRequiredException();
        }
    }

}
