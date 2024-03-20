package dev.luisjohann.checklist.domain.project;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import dev.luisjohann.checklist.domain.project.exceptions.InvalidWorkerNameException;
import dev.luisjohann.checklist.domain.project.exceptions.ProjectRequiredException;

public class WorkerTest {

    static Project project;

    @BeforeAll
    static void start() {
        project = mock(Project.class);
    }

    @Test
    void testCreateWorkerWithEmptySlug_thenRetrieveInvalidWorkerNameException() {
        var thrown = assertThrows(InvalidWorkerNameException.class, () -> {
            new Worker(0L, "", "Name", project);
        }, "InvalidWorkerNameException was expected");

        assertEquals("Worker name invalid", thrown.getTitle());
        assertEquals("Worker name must be informed!", thrown.getMessage());
    }

    @Test
    void testCreateWorkerWithNullSlug_thenRetrieveInvalidWorkerNameException() {
        var thrown = assertThrows(InvalidWorkerNameException.class, () -> {
            new Worker(0L, null, "Name", project);
        }, "InvalidWorkerNameException was expected");

        assertEquals("Worker name invalid", thrown.getTitle());
        assertEquals("Worker name must be informed!", thrown.getMessage());
    }

    @Test
    void testCreateWorkerWithNullName_thenRetrieveInvalidWorkerNameException() {
        var thrown = assertThrows(InvalidWorkerNameException.class, () -> {
            new Worker(0L, "Slug", null, project);
        }, "InvalidWorkerNameException was expected");

        assertEquals("Worker name invalid", thrown.getTitle());
        assertEquals("Worker name must be informed!", thrown.getMessage());
    }

    @Test
    void testCreateWorkerWithEmptyName_thenRetrieveInvalidWorkerNameException() {
        var thrown = assertThrows(InvalidWorkerNameException.class, () -> {
            new Worker(0L, "Slug", "", project);
        }, "InvalidWorkerNameException was expected");

        assertEquals("Worker name invalid", thrown.getTitle());
        assertEquals("Worker name must be informed!", thrown.getMessage());
    }

    @Test
    void testCreateWorkerWithNullProject_thenRetrieveProjectRequiredException() {
        var thrown = assertThrows(ProjectRequiredException.class, () -> {
            new Worker(0L, "Slug", "Name", null);
        }, "ProjectRequiredException was expected");

        assertEquals("Project is required", thrown.getTitle());
        assertEquals("The project must be informed!", thrown.getMessage());
    }

    @Test
    void testCreateWorkerWithIdNull() {
        var worker = new Worker(null, "Slug", "Name", project);

        assertNull(worker.id());
        assertEquals("Slug", worker.slug());
        assertEquals("Name", worker.name());
        assertEquals(project, worker.project());
    }

    @Test
    void testCreateWorkerWithIdEmpty() {
        var worker = new Worker(0L, "Slug", "Name", project);

        assertEquals(0L, worker.id());
        assertEquals("Slug", worker.slug());
        assertEquals("Name", worker.name());
    }
}
