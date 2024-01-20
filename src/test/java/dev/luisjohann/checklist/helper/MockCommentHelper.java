package dev.luisjohann.checklist.helper;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import org.apache.commons.lang3.RandomStringUtils;

import dev.luisjohann.checklist.domain.project.Worker;
import dev.luisjohann.checklist.domain.todo.Comment;
import dev.luisjohann.checklist.domain.todo.ICommentRepository;
import dev.luisjohann.checklist.domain.todo.Todo;

public class MockCommentHelper {

    public static final Comment createBean(final ICommentRepository repository, final Todo todo,
            final Worker createdWorker) {
        var bean = createNotPersistBean(todo, createdWorker);
        repository.createComment(bean);
        return bean;
    }

    public static final Comment createNotPersistBean(final Todo todo, final Worker createdWorker) {
        return new Comment(UUID.randomUUID().toString(), todo, RandomStringUtils.randomAlphabetic(15),
                createdWorker, LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS), null, null, null, null);
    }

}
