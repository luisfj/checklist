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
import dev.luisjohann.checklist.domain.project.exceptions.WorkerRequiredException;
import dev.luisjohann.checklist.domain.project.exceptions.WorkerWithSlugNotFoundException;
import dev.luisjohann.checklist.domain.todo.ITodoRepository;
import dev.luisjohann.checklist.domain.todo.Todo;
import dev.luisjohann.checklist.domain.todo.exception.TodoNotFoundException;
import dev.luisjohann.checklist.helper.MockProjectHelper;
import dev.luisjohann.checklist.helper.MockTodoHelper;
import dev.luisjohann.checklist.helper.MockWorkerHelper;
import dev.luisjohann.checklist.infra.todo.controller.request.AddCommentRequest;
import dev.luisjohann.checklist.infra.todo.controller.response.AddCommentResponse;

public class AddCommentControllerTest extends ChecklistApplicationBaseTest {
    private static final String URI = "/comment/%s/%s";

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
    private Todo todoNotExist;
    private String comment;

    @BeforeAll
    public void start() {
        project = MockProjectHelper.createBean(projectRepository);
        otherProject = MockProjectHelper.createBean(projectRepository);
        worker = MockWorkerHelper.createBean(workerRepository, project);
        otherWorker = MockWorkerHelper.createBean(workerRepository, otherProject);
        todo = MockTodoHelper.createBean(todoRepository, project, null);
        todoNotExist = MockTodoHelper.createNotPersistBean(project, null);
        comment = "Test Todo Comment";
    }

    String buildUri(String projectSlug, String todoId) {
        return String.format(URI, projectSlug, todoId);
    }

    @Test
    void testAddCommentToTodoWithOtherProjectSlug_returnTodoNotFoundException() {
        var request = new AddCommentRequest(comment, worker.slug());
        var uri = buildUri(otherProject.slug(), todo.id());

        webTestClient
                .post()
                .uri(uri)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(TodoNotFoundException.class);
    }

    @Test
    void testAddCommentToNotExistTodo_returnTodoNotFoundException() {
        var request = new AddCommentRequest(comment, worker.slug());
        var uri = buildUri(project.slug(), todoNotExist.id());

        webTestClient
                .post()
                .uri(uri)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(TodoNotFoundException.class);
    }

    @Test
    void testAddCommentToTodoWithWorkerOfOtherProject_returnWorkerNotFoundException() {
        var request = new AddCommentRequest(comment, otherWorker.slug());
        var uri = buildUri(project.slug(), todo.id());

        webTestClient
                .post()
                .uri(uri)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(WorkerWithSlugNotFoundException.class);
    }

    @Test
    void testAddCommentToTodoWithoutWorker_returnWorkerRequiredException() {
        var request = new AddCommentRequest(comment, "");
        var uri = buildUri(project.slug(), todo.id());

        webTestClient
                .post()
                .uri(uri)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(WorkerRequiredException.class);
    }

    @Test
    void shouldAddCommentToTodoWithWorker() {
        var request = new AddCommentRequest(comment, worker.slug());
        var uri = buildUri(project.slug(), todo.id());

        webTestClient
                .post()
                .uri(uri)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(AddCommentResponse.class)
                .value(resp -> {
                    assertNotNull(resp.id());
                    assertEquals(request.comment(), resp.comment());
                    assertEquals(worker.slug(), resp.createdWorkerSlug());
                    assertEquals(worker.name(), resp.createdWorkerName());
                    assertNull(resp.deleteWorkerName());
                    assertNull(resp.deleteWorkerSlug());
                    assertNull(resp.deletedAt());
                });
    }
}
