package dev.luisjohann.checklist.infra.todo.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

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
import dev.luisjohann.checklist.infra.todo.controller.request.UncheckTodoRequest;
import dev.luisjohann.checklist.infra.todo.controller.response.UncheckTodoResponse;

public class UncheckTodoControllerTest extends ChecklistApplicationBaseTest {
    private static final String URI = "/todo-uncheck/%s/%s";

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
        todo = MockTodoHelper.createCompleteBean(todoRepository, project, worker);
    }

    @AfterEach
    void endEach() {
        todoRepository.removeTodo(todo);
        todo = null;
    }

    String buildUri(String projectSlug, String todoId) {
        return String.format(URI, projectSlug, todoId);
    }

    @Test
    void testUncheckTodoWithOtherProjectSlug_returnProjectNotFoundException() {
        var request = new UncheckTodoRequest(worker.getSlug());

        webTestClient
                .patch()
                .uri(buildUri(otherProject.getSlug(), todo.getId()))
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(ProjectNotFoundException.class);
    }

    @Test
    void testUncheckTodoWithWorkerOfOtherProject_returnWorkerNotFoundException() {
        var request = new UncheckTodoRequest(otherProjectWorker.getSlug());

        webTestClient
                .patch()
                .uri(buildUri(project.getSlug(), todo.getId()))
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(WorkerWithSlugNotFoundException.class);
    }

    @Test
    void testUncheckTodoWithoutWorker_returnWorkerRequiredException() {
        var request = new UncheckTodoRequest(null);

        webTestClient
                .patch()
                .uri(buildUri(project.getSlug(), todo.getId()))
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(WorkerRequiredException.class);
    }

    @Test
    void shouldUncheckTodoWithAssignedAndCheckedSameWorker() {
        var request = new UncheckTodoRequest(worker.getSlug());

        webTestClient
                .patch()
                .uri(buildUri(project.getSlug(), todo.getId()))
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isOk()
                .expectBody(UncheckTodoResponse.class)
                .value(resp -> {
                    assertEquals(todo.getId(), resp.id());
                    assertEquals(todo.getTitle(), resp.title());
                    assertEquals(todo.getDescription(), resp.description());
                    assertNotNull(resp.workerAssignedSlug());
                    assertEquals(worker.getSlug(), resp.workerAssignedSlug());
                    assertEquals(worker.getName(), resp.workerAssignedName());
                    assertNotNull(todo.getCheckedWorker());
                    assertNull(resp.workerCheckedSlug());
                    assertNull(resp.workerCheckedSlug());
                    assertNull(resp.workerCheckedName());
                });
    }

    @Test
    void shouldUncheckTodoWithAssignedAndCheckedOtherWorker() {
        var request = new UncheckTodoRequest(otherWorker.getSlug());

        webTestClient
                .patch()
                .uri(buildUri(project.getSlug(), todo.getId()))
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isOk()
                .expectBody(UncheckTodoResponse.class)
                .value(resp -> {
                    assertEquals(todo.getId(), resp.id());
                    assertEquals(todo.getTitle(), resp.title());
                    assertEquals(todo.getDescription(), resp.description());
                    assertNotNull(resp.workerAssignedSlug());
                    assertEquals(worker.getSlug(), resp.workerAssignedSlug());
                    assertEquals(worker.getName(), resp.workerAssignedName());
                    assertNotNull(todo.getCheckedWorker());
                    assertNull(resp.workerCheckedSlug());
                    assertNull(resp.workerCheckedSlug());
                    assertNull(resp.workerCheckedName());
                });
    }

}