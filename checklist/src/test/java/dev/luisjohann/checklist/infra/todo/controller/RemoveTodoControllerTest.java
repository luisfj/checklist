package dev.luisjohann.checklist.infra.todo.controller;

import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import dev.luisjohann.checklist.ChecklistApplicationBaseTest;
import dev.luisjohann.checklist.domain.project.IProjectRepository;
import dev.luisjohann.checklist.domain.project.Project;
import dev.luisjohann.checklist.domain.project.exceptions.ProjectNotFoundException;
import dev.luisjohann.checklist.domain.todo.ITodoRepository;
import dev.luisjohann.checklist.domain.todo.Todo;
import dev.luisjohann.checklist.domain.todo.exception.TodoNotFoundException;
import dev.luisjohann.checklist.helper.MockProjectHelper;
import dev.luisjohann.checklist.helper.MockTodoHelper;

public class RemoveTodoControllerTest extends ChecklistApplicationBaseTest {
    private static final String URI = "/todo/%s/%s";

    @Autowired
    private IProjectRepository projectRepository;
    @Autowired
    private ITodoRepository todoRepository;

    private Project project;
    private Project projectInvalid;

    private Todo todo;
    private Todo todoInvalid;

    @BeforeAll
    void start() {
        project = MockProjectHelper.createBean(projectRepository);
        projectInvalid = MockProjectHelper.createNotPersistedBean();
    }

    @BeforeEach
    void createTodo() {
        todo = MockTodoHelper.createBean(todoRepository, project, null);
        todoInvalid = MockTodoHelper.createNotPersistBean(project, null);
    }

    @AfterEach
    void removeTodo() {
        todoRepository.removeTodo(todo);
        todo = null;
    }

    @Test
    void testRemoveWithProjectNotExists_thenReturnProjectNotFoundException() {
        var requestUri = buildUri(todo.id(), projectInvalid.slug());

        webTestClient
                .delete()
                .uri(requestUri)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(ProjectNotFoundException.class);
    }

    @Test
    void testRemoveWithTodoIdNotExists_thenReturnTodoNotFoundException() {
        var requestUri = buildUri(todoInvalid.id(), project.slug());

        webTestClient
                .delete()
                .uri(requestUri)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(TodoNotFoundException.class);
    }

    @Test
    void shouldRemoveTodo() {
        var requestUri = buildUri(todo.id(), project.slug());

        webTestClient
                .delete()
                .uri(requestUri)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk();

        assertNull(todoRepository.findByIdAndProjectSlug(todo.id(), project.slug()).block());
    }

    String buildUri(String todoId, String projectSlug) {
        return String.format(URI, projectSlug, todoId);
    }
}
