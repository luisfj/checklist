package dev.luisjohann.checklist.infra.todo.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
import dev.luisjohann.checklist.infra.todo.controller.request.AddWorkerToTodoRequest;
import dev.luisjohann.checklist.infra.todo.controller.response.AddWorkerToTodoResponse;

public class AddWorkerToTodoControllerTest extends ChecklistApplicationBaseTest {
    private static final String URI = "/todo-worker-add/%s/%s";

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
    void testAddWorkerToTodoWithOtherProjectSlug_returnProjectNotFoundException() {
        var request = new AddWorkerToTodoRequest(worker.slug());
        var uri = buildUri(otherProject.slug(), todo.id());

        webTestClient
                .patch()
                .uri(uri)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(ProjectNotFoundException.class);
    }

    @Test
    void testAddWorkerToTodoWithWorkerOfOtherProject_returnWorkerNotFoundException() {
        var request = new AddWorkerToTodoRequest(otherWorker.slug());
        var uri = buildUri(project.slug(), todo.id());

        webTestClient
                .patch()
                .uri(uri)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(WorkerWithSlugNotFoundException.class);
    }

    @Test
    void testAddWorkerToTodoWithoutWorker_returnWorkerRequiredException() {
        var request = new AddWorkerToTodoRequest("");
        var uri = buildUri(project.slug(), todo.id());

        webTestClient
                .patch()
                .uri(uri)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(WorkerRequiredException.class);
    }

    @Test
    void shouldAddWorkerToTodoWithWorker() {
        var request = new AddWorkerToTodoRequest(worker.slug());
        var uri = buildUri(project.slug(), todo.id());

        webTestClient
                .patch()
                .uri(uri)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isOk()
                .expectBody(AddWorkerToTodoResponse.class)
                .value(resp -> {
                    assertEquals(todo.id(), UUID.fromString(resp.id()));
                    assertEquals(todo.title(), resp.title());
                    assertEquals(todo.description(), resp.description());
                    assertEquals(worker.slug(), resp.workerAssignedSlug());
                    assertEquals(worker.name(), resp.workerAssignedName());
                    assertNull(todo.assignedTo());
                });
    }
}
