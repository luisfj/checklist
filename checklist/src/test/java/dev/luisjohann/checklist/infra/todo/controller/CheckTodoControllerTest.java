package dev.luisjohann.checklist.infra.todo.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
import dev.luisjohann.checklist.domain.project.exceptions.WorkerRequiredException;
import dev.luisjohann.checklist.domain.project.exceptions.WorkerWithSlugNotFoundException;
import dev.luisjohann.checklist.domain.todo.ITodoRepository;
import dev.luisjohann.checklist.domain.todo.Todo;
import dev.luisjohann.checklist.helper.MockProjectHelper;
import dev.luisjohann.checklist.helper.MockTodoHelper;
import dev.luisjohann.checklist.helper.MockWorkerHelper;
import dev.luisjohann.checklist.infra.todo.controller.request.CheckTodoRequest;
import dev.luisjohann.checklist.infra.todo.controller.response.CheckTodoResponse;

public class CheckTodoControllerTest extends ChecklistApplicationBaseTest {
    private static final String URI = "/todo-check/%s/%s";

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
    private Worker otherProjectWorker;
    private Todo todo;

    @BeforeAll
    public void start() {
        project = MockProjectHelper.createBean(projectRepository);
        otherProject = MockProjectHelper.createBean(projectRepository);
        worker = MockWorkerHelper.createBean(workerRepository, project);
        otherWorker = MockWorkerHelper.createBean(workerRepository, project);
        otherProjectWorker = MockWorkerHelper.createBean(workerRepository, otherProject);
    }

    @BeforeEach
    void startEach() {
        todo = MockTodoHelper.createBean(todoRepository, project, worker);
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
    void testCheckTodoWithOtherProjectSlug_returnProjectNotFoundException() {
        var request = new CheckTodoRequest(worker.slug());

        webTestClient
                .patch()
                .uri(buildUri(otherProject.slug(), todo.id()))
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(ProjectNotFoundException.class);
    }

    @Test
    void testCheckTodoWithWorkerOfOtherProject_returnWorkerNotFoundException() {
        var request = new CheckTodoRequest(otherProjectWorker.slug());

        webTestClient
                .patch()
                .uri(buildUri(project.slug(), todo.id()))
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(WorkerWithSlugNotFoundException.class);
    }

    @Test
    void testCheckTodoWithoutWorker_returnWorkerRequiredException() {
        var request = new CheckTodoRequest(null);

        webTestClient
                .patch()
                .uri(buildUri(project.slug(), todo.id()))
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(WorkerRequiredException.class);
    }

    @Test
    void shouldCheckTodoWithAssignedAndCheckedSameWorker() {
        var request = new CheckTodoRequest(worker.slug());

        webTestClient
                .patch()
                .uri(buildUri(project.slug(), todo.id()))
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isOk()
                .expectBody(CheckTodoResponse.class)
                .value(resp -> {
                    assertEquals(todo.id(), UUID.fromString(resp.id()));
                    assertEquals(todo.title(), resp.title());
                    assertEquals(todo.description(), resp.description());
                    assertNotNull(resp.workerAssignedSlug());
                    assertEquals(worker.slug(), resp.workerAssignedSlug());
                    assertEquals(worker.name(), resp.workerAssignedName());
                    assertNull(todo.checkedWorker());
                    assertNotNull(resp.workerCheckedSlug());
                    assertNotNull(resp.checkedAt());
                    assertEquals(worker.slug(), resp.workerCheckedSlug());
                    assertEquals(worker.name(), resp.workerCheckedName());
                });
    }

    @Test
    void shouldCheckTodoWithAssignedAndCheckedOtherWorker() {
        var request = new CheckTodoRequest(otherWorker.slug());

        webTestClient
                .patch()
                .uri(buildUri(project.slug(), todo.id()))
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isOk()
                .expectBody(CheckTodoResponse.class)
                .value(resp -> {
                    assertEquals(todo.id(), UUID.fromString(resp.id()));
                    assertEquals(todo.title(), resp.title());
                    assertEquals(todo.description(), resp.description());
                    assertNotNull(resp.workerAssignedSlug());
                    assertEquals(worker.slug(), resp.workerAssignedSlug());
                    assertEquals(worker.name(), resp.workerAssignedName());
                    assertNull(todo.checkedWorker());
                    assertNotNull(resp.workerCheckedSlug());
                    assertEquals(otherWorker.slug(), resp.workerCheckedSlug());
                    assertEquals(otherWorker.name(), resp.workerCheckedName());
                });
    }

}