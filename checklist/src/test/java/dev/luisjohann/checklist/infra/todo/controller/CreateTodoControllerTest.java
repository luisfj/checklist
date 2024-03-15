package dev.luisjohann.checklist.infra.todo.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.BeforeAll;
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
import dev.luisjohann.checklist.infra.todo.controller.request.CreateTodoRequest;
import dev.luisjohann.checklist.infra.todo.controller.response.CreateTodoResponse;

public class CreateTodoControllerTest extends ChecklistApplicationBaseTest {
    private static final String URI = "/todo/";
    private static final String PROJECT_SLUG = "test_project";
    private static final String PROJECT_OTHER_SLUG = "test_project_other";
    private static final String PROJECT_INVALID_SLUG = "test_project_not_exists";
    private static final String WORKER_SLUG = "worker_test";
    private static final String WORKER_NAME = "Worker Name Test";
    private static final String WORKER_SLUG_INVALID = "worker_test_not_exists";

    @Autowired
    private IProjectRepository projectRepository;
    @Autowired
    private IWorkerRepository workerRepository;

    @BeforeAll
    public void start() {
        var project = new Project(null, PROJECT_SLUG, PROJECT_SLUG, null);
        projectRepository.createProject(project);
        projectRepository.createProject(new Project(null, PROJECT_OTHER_SLUG, PROJECT_OTHER_SLUG, null));
        workerRepository.createWorker(new Worker(null, WORKER_SLUG, WORKER_NAME, project));
    }

    @Test
    void testAddTodoInNotExistsProject_returnProjectNotFoundException() {
        var createTodoRequest = buildTodo(WORKER_SLUG);

        webTestClient
                .post()
                .uri(URI + PROJECT_INVALID_SLUG)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(createTodoRequest)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(ProjectNotFoundException.class);
    }

    @Test
    void testAddTodoWithWorkerOfOtherProject_returnWorkerNotFoundException() {
        var createTodoRequest = buildTodo(WORKER_SLUG);

        webTestClient
                .post()
                .uri(URI + PROJECT_OTHER_SLUG)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(createTodoRequest)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(WorkerWithSlugNotFoundException.class);
    }

    @Test
    void testAddTodoWithNotExistsWorker_returnWorkerWithSlugNotFoundException() {
        var createTodoRequest = buildTodo(WORKER_SLUG_INVALID);

        webTestClient
                .post()
                .uri(URI + PROJECT_SLUG)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(createTodoRequest)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(WorkerWithSlugNotFoundException.class);
    }

    @Test
    void shouldAddTodoWithoutWorker() {
        var createTodoRequest = buildTodo(null);

        webTestClient
                .post()
                .uri(URI + PROJECT_SLUG)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(createTodoRequest)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(CreateTodoResponse.class)
                .value(resp -> {
                    assertEquals(resp.title(), createTodoRequest.title());
                    assertEquals(resp.description(), createTodoRequest.description());
                    assertNull(resp.workerAssignedSlug());
                    assertNull(resp.workerAssignedName());
                });
    }

    @Test
    void shouldAddTodoWithWorker() {
        var createTodoRequest = buildTodo(WORKER_SLUG);

        webTestClient
                .post()
                .uri(URI + PROJECT_SLUG)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(createTodoRequest)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(CreateTodoResponse.class)
                .value(resp -> {
                    assertEquals(resp.title(), createTodoRequest.title());
                    assertEquals(resp.description(), createTodoRequest.description());
                    assertNotNull(resp.workerAssignedSlug());
                    assertNotNull(resp.workerAssignedName());
                    assertEquals(resp.workerAssignedSlug(), createTodoRequest.workerAssignedSlug());
                    assertEquals(resp.workerAssignedName(), WORKER_NAME);
                });
    }

    CreateTodoRequest buildTodo(String workerAssignedSlug) {
        return new CreateTodoRequest("title todo test", "description todo test", workerAssignedSlug);
    }
}
