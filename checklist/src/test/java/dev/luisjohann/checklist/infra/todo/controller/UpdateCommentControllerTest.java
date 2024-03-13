package dev.luisjohann.checklist.infra.todo.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.apache.commons.lang3.RandomStringUtils;
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
import dev.luisjohann.checklist.domain.project.exceptions.WorkerRequiredException;
import dev.luisjohann.checklist.domain.project.exceptions.WorkerWithSlugNotFoundException;
import dev.luisjohann.checklist.domain.todo.Comment;
import dev.luisjohann.checklist.domain.todo.ICommentRepository;
import dev.luisjohann.checklist.domain.todo.ITodoRepository;
import dev.luisjohann.checklist.domain.todo.Todo;
import dev.luisjohann.checklist.domain.todo.exception.CommentNotFoundException;
import dev.luisjohann.checklist.helper.MockCommentHelper;
import dev.luisjohann.checklist.helper.MockProjectHelper;
import dev.luisjohann.checklist.helper.MockTodoHelper;
import dev.luisjohann.checklist.helper.MockWorkerHelper;
import dev.luisjohann.checklist.infra.todo.controller.request.UpdateCommentRequest;
import dev.luisjohann.checklist.infra.todo.controller.response.UpdateCommentResponse;

public class UpdateCommentControllerTest extends ChecklistApplicationBaseTest {
    private static final String URI = "/comment/%s/%s/%s";

    @Autowired
    private IProjectRepository projectRepository;
    @Autowired
    private IWorkerRepository workerRepository;
    @Autowired
    private ITodoRepository todoRepository;
    @Autowired
    private ICommentRepository commentRepository;

    private Project project;
    private Project otherProject;
    private Worker worker;
    private Worker otherWorker;
    private Worker otherProjectWorker;
    private Todo todo;
    private Todo todoNotExist;
    private Comment comment;
    private Comment commentNotExists;
    private String newComment;

    @BeforeAll
    public void start() {
        project = MockProjectHelper.createBean(projectRepository);
        otherProject = MockProjectHelper.createBean(projectRepository);
        worker = MockWorkerHelper.createBean(workerRepository, project);
        otherWorker = MockWorkerHelper.createBean(workerRepository, project);
        otherProjectWorker = MockWorkerHelper.createBean(workerRepository, otherProject);
        todo = MockTodoHelper.createBean(todoRepository, project, null);
        todoNotExist = MockTodoHelper.createNotPersistBean(project, null);
        commentNotExists = MockCommentHelper.createNotPersistBean(todo, worker);
    }

    @BeforeEach
    void startEach() {
        comment = MockCommentHelper.createBean(commentRepository, todo, worker);
        newComment = RandomStringUtils.randomAlphabetic(7);
    }

    @AfterEach
    void endEach() {
        commentRepository.removeComment(comment);
        comment = null;
    }

    String buildUri(String projectSlug, String todoId, String id) {
        return String.format(URI, projectSlug, todoId, id);
    }

    @Test
    void testUpdateCommentWithOtherProjectSlug_returnCommentNotFoundException() {
        var request = new UpdateCommentRequest(newComment, otherWorker.getSlug());
        var uri = buildUri(otherProject.getSlug(), todo.getId(), comment.id());

        webTestClient
                .put()
                .uri(uri)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(CommentNotFoundException.class);
    }

    @Test
    void testUpdateCommentNotExistTodo_returnCommentNotFoundException() {
        var request = new UpdateCommentRequest(newComment, otherWorker.getSlug());
        var uri = buildUri(project.getSlug(), todoNotExist.getId(), comment.id());

        webTestClient
                .put()
                .uri(uri)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(CommentNotFoundException.class);
    }

    @Test
    void testUpdateCommentWithNotPersistCommentId_returnCommentNotFoundException() {
        var request = new UpdateCommentRequest(newComment, otherWorker.getSlug());
        var uri = buildUri(project.getSlug(), todoNotExist.getId(), commentNotExists.id());

        webTestClient
                .put()
                .uri(uri)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(CommentNotFoundException.class);
    }

    @Test
    void testUpdateCommentWithWorkerOfOtherProject_returnWorkerNotFoundException() {
        var request = new UpdateCommentRequest(newComment, otherProjectWorker.getSlug());
        var uri = buildUri(project.getSlug(), todo.getId(), comment.id());

        webTestClient
                .put()
                .uri(uri)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(WorkerWithSlugNotFoundException.class);
    }

    @Test
    void testUpdateCommentWithoutWorker_returnWorkerRequiredException() {
        var request = new UpdateCommentRequest(newComment, "");
        var uri = buildUri(project.getSlug(), todo.getId(), comment.id());

        webTestClient
                .put()
                .uri(uri)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(WorkerRequiredException.class);
    }

    @Test
    void shouldUpdateComment() {
        var request = new UpdateCommentRequest(newComment, otherWorker.getSlug());
        var uri = buildUri(project.getSlug(), todo.getId(), comment.id());

        webTestClient
                .put()
                .uri(uri)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isOk()
                .expectBody(UpdateCommentResponse.class)
                .value(resp -> {
                    assertEquals(comment.id(), resp.id());
                    assertNotEquals(comment.comment(), resp.comment());
                    assertEquals(request.comment(), resp.comment());
                    assertEquals(worker.getSlug(), resp.createdWorkerSlug());
                    assertEquals(worker.getName(), resp.createdWorkerName());
                    assertEquals(otherWorker.getName(), resp.updatedWorkerName());
                    assertEquals(otherWorker.getName(), resp.updatedWorkerName());
                    assertNull(resp.deleteWorkerName());
                    assertNull(resp.deleteWorkerSlug());
                    assertNull(resp.deletedAt());
                    assertNotNull(resp.updatedAt());
                });
    }
}
