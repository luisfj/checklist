package dev.luisjohann.checklist.infra.todo.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.UUID;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import dev.luisjohann.checklist.ChecklistApplicationBaseTest;
import dev.luisjohann.checklist.domain.project.IProjectRepository;
import dev.luisjohann.checklist.domain.project.IWorkerRepository;
import dev.luisjohann.checklist.domain.project.Project;
import dev.luisjohann.checklist.domain.project.Worker;
import dev.luisjohann.checklist.domain.project.exceptions.ProjectNotFoundException;
import dev.luisjohann.checklist.domain.project.exceptions.WorkerWithSlugNotFoundException;
import dev.luisjohann.checklist.domain.todo.ITodoRepository;
import dev.luisjohann.checklist.domain.todo.Todo;
import dev.luisjohann.checklist.helper.MockProjectHelper;
import dev.luisjohann.checklist.helper.MockTodoHelper;
import dev.luisjohann.checklist.helper.MockWorkerHelper;
import dev.luisjohann.checklist.infra.todo.controller.request.UpdateTodoRequest;
import dev.luisjohann.checklist.infra.todo.controller.response.UpdateTodoResponse;

public class UpdateTodoControllerTest extends ChecklistApplicationBaseTest {
    private static final String URI = "/todo/%s/%s";

    private static final String NEW_TITLE = "Updated title";
    private static final String NEW_DESCRIPTION = "Updated description";

    @Autowired
    private IProjectRepository projectRepository;
    @Autowired
    private IWorkerRepository workerRepository;
    @Autowired
    private ITodoRepository todoRepository;

    private Project project;
    private Project otherProject;
    private Worker worker;
    private Worker otherWorker;
    private Todo todo;

    @BeforeAll
    public void start() {
        project = MockProjectHelper.createBean(projectRepository);
        otherProject = MockProjectHelper.createBean(projectRepository);
        worker = MockWorkerHelper.createBean(workerRepository, project);
        otherWorker = MockWorkerHelper.createBean(workerRepository, otherProject);
    }

    @BeforeEach
    void startEach() {
        todo = MockTodoHelper.createBean(todoRepository, project, null);
    }

    @AfterEach
    void endEach() {
        todoRepository.removeTodo(todo);
        todo = null;
    }

    String buildUri(String projectSlug, UUID todoId) {
        return String.format(URI, projectSlug, todoId);
    }

    @Test
    void testUpdateTodoWithOtherProjectSlug_returnProjectNotFoundException() {
        var request = new UpdateTodoRequest(NEW_TITLE, NEW_DESCRIPTION, worker.slug());

        webTestClient
                .put()
                .uri(buildUri(otherProject.slug(), todo.id()))
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(ProjectNotFoundException.class);
    }

    @Test
    void testUpdateTodoWithWorkerOfOtherProject_returnWorkerNotFoundException() {
        var request = new UpdateTodoRequest(NEW_TITLE, NEW_DESCRIPTION, otherWorker.slug());

        webTestClient
                .put()
                .uri(buildUri(project.slug(), todo.id()))
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(WorkerWithSlugNotFoundException.class);
    }

    @Test
    void shouldUpdateTodoWithWorker() {
        var request = new UpdateTodoRequest(NEW_TITLE, NEW_DESCRIPTION, worker.slug());

        webTestClient
                .put()
                .uri(buildUri(project.slug(), todo.id()))
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isOk()
                .expectBody(UpdateTodoResponse.class)
                .value(resp -> {
                    assertEquals(todo.id(), UUID.fromString(resp.id()));
                    assertEquals(request.title(), resp.title());
                    assertEquals(request.description(), resp.description());
                    assertNotEquals(todo.title(), resp.title());
                    assertNotEquals(todo.description(), resp.description());
                    assertEquals(worker.slug(), resp.workerAssignedSlug());
                    assertEquals(worker.name(), resp.workerAssignedName());
                    assertNull(todo.assignedTo());
                    assertNotNull(resp.workerAssignedSlug());
                });
    }

    @Test
    void shouldUpdateTodoWithoutWorker() {
        var request = new UpdateTodoRequest(NEW_TITLE, NEW_DESCRIPTION, null);

        webTestClient
                .put()
                .uri(buildUri(project.slug(), todo.id()))
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isOk()
                .expectBody(UpdateTodoResponse.class)
                .value(resp -> {
                    assertEquals(todo.id(), UUID.fromString(resp.id()));
                    assertEquals(request.title(), resp.title());
                    assertEquals(request.description(), resp.description());
                    assertNotEquals(todo.title(), resp.title());
                    assertNotEquals(todo.description(), resp.description());
                    assertNull(todo.assignedTo());
                    assertNull(resp.workerAssignedSlug());
                    assertNull(resp.workerAssignedName());
                });
    }
}
