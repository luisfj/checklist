package dev.luisjohann.checklist.helper;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import org.apache.commons.lang3.RandomStringUtils;

import dev.luisjohann.checklist.domain.project.Project;
import dev.luisjohann.checklist.domain.project.Worker;
import dev.luisjohann.checklist.domain.todo.ITodoRepository;
import dev.luisjohann.checklist.domain.todo.Todo;

public class MockTodoHelper {

    public static final Todo createBean(final ITodoRepository repository, final Project project,
            final Worker assignedTo) {
        var bean = createNotPersistBean(project, assignedTo);
        repository.createTodo(bean);
        return bean;
    }

    public static final Todo createCompleteBean(final ITodoRepository repository, final Project project,
            final Worker assignedTo) {
        var bean = createNotPersistCompleteBean(project, assignedTo);
        repository.createTodo(bean);
        return bean;
    }

    public static final Todo createNotPersistBean(final Project project, final Worker assignedTo) {
        return new Todo(UUID.randomUUID().toString(), RandomStringUtils.randomAlphabetic(7).toUpperCase(),
                RandomStringUtils.randomAlphabetic(20).toUpperCase(), project, assignedTo,
                LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS),
                null,
                null, null);
    }

    public static final Todo createNotPersistCompleteBean(final Project project, final Worker assignedTo) {
        return new Todo(UUID.randomUUID().toString(), RandomStringUtils.randomAlphabetic(7).toUpperCase(),
                RandomStringUtils.randomAlphabetic(20).toUpperCase(), project, assignedTo,
                LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS),
                LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS),
                LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS), assignedTo);
    }

}
