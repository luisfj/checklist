package dev.luisjohann.checklist.domain.todo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;

import java.time.LocalDateTime;
import java.util.UUID;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import dev.luisjohann.checklist.domain.project.Project;
import dev.luisjohann.checklist.domain.project.Worker;
import dev.luisjohann.checklist.domain.project.exceptions.ProjectRequiredException;
import dev.luisjohann.checklist.domain.todo.exception.EmptyTodoDescriptionException;
import dev.luisjohann.checklist.domain.todo.exception.EmptyTodoTitleException;

public class TodoTest {

    static Project project;
    static Worker worker;

    @BeforeAll
    static void start() {
        project = mock(Project.class);
        worker = mock(Worker.class);
    }

    @Test
    void testCreateTodoWithEmptyTitleOrDescriptionOrProject_thenRetrieveException() {
        var thrown = assertThrows(EmptyTodoTitleException.class, () -> {
            new Todo(UUID.randomUUID(), "", "Description",
                    project, worker, LocalDateTime.now(), null, null, null);
        }, "EmptyTodoTitleException was expected");

        assertEquals("Title is required", thrown.getTitle());
        assertEquals("Title cannot be empty!", thrown.getMessage());

        thrown = assertThrows(EmptyTodoTitleException.class, () -> {
            new Todo(UUID.randomUUID(), null, "Description",
                    project, worker, LocalDateTime.now(), null, null, null);
        }, "EmptyTodoTitleException was expected");

        assertEquals("Title is required", thrown.getTitle());
        assertEquals("Title cannot be empty!", thrown.getMessage());

        var thrownDescription = assertThrows(EmptyTodoDescriptionException.class, () -> {
            new Todo(UUID.randomUUID(), "Title", "",
                    project, worker, LocalDateTime.now(), null, null, null);
        }, "EmptyTodoDescriptionException was expected");

        assertEquals("Description is required", thrownDescription.getTitle());
        assertEquals("Description cannot be empty!", thrownDescription.getMessage());

        thrownDescription = assertThrows(EmptyTodoDescriptionException.class, () -> {
            new Todo(UUID.randomUUID(), "Title", null,
                    project, worker, LocalDateTime.now(), null, null, null);
        }, "EmptyTodoDescriptionException was expected");

        assertEquals("Description is required", thrownDescription.getTitle());
        assertEquals("Description cannot be empty!", thrownDescription.getMessage());

        var thrownProject = assertThrows(ProjectRequiredException.class, () -> {
            new Todo(UUID.randomUUID(), "Title", "Description",
                    null, worker, LocalDateTime.now(), null, null, null);
        }, "ProjectRequiredException was expected");

        assertEquals("Project is required", thrownProject.getTitle());
        assertEquals("The project must be informed!", thrownProject.getMessage());
    }

    @Test
    void testCreateTodo() {
        UUID id = UUID.randomUUID();
        LocalDateTime dtHora = LocalDateTime.now();

        var todo = new Todo(id, "Title", "Description", project, worker, dtHora, dtHora, dtHora, worker);

        assertEquals(id, todo.id());
        assertEquals("Title", todo.title());
        assertEquals("Description", todo.description());
        assertEquals(project, todo.project());
        assertEquals(worker, todo.assignedTo());
        assertEquals(worker, todo.checkedWorker());
        assertEquals(dtHora, todo.checkedAt());
        assertEquals(dtHora, todo.updatedAt());
        assertEquals(dtHora, todo.checkedAt());

        todo = new Todo(null, "Title", "Description", project, null, null, null, null, null);

        assertNull(todo.id());
        assertEquals("Title", todo.title());
        assertEquals("Description", todo.description());
        assertEquals(project, todo.project());
        assertNull(todo.assignedTo());
        assertNull(todo.checkedWorker());
        assertNull(todo.checkedAt());
        assertNull(todo.updatedAt());
        assertNull(todo.checkedAt());
    }
}
