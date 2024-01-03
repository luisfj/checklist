package dev.luisjohann.checklist.helper;

import java.time.LocalDateTime;
import java.util.UUID;

import dev.luisjohann.checklist.domain.project.Project;
import dev.luisjohann.checklist.domain.project.Worker;
import dev.luisjohann.checklist.domain.todo.ITodoRepository;
import dev.luisjohann.checklist.domain.todo.Todo;

public class MockTodoHelper {

    private static final String TITLE = "Todo Title Test";
    private static final String DESCRIPTION = "Description of TODO for tests";

    public static final Todo createBean(final ITodoRepository repository, final Project project,
            final Worker assignedTo) {
        var bean = createNotPersistBean(project, assignedTo);
        repository.createTodo(bean);
        return bean;
    }

    public static final Todo createNotPersistBean(final Project project, final Worker assignedTo) {
        return new Todo(UUID.randomUUID().toString(), TITLE, DESCRIPTION, project, assignedTo, LocalDateTime.now(),
                null,
                null, null);
    }

}
