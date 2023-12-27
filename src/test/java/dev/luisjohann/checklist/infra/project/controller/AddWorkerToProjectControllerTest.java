package dev.luisjohann.checklist.infra.project.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import dev.luisjohann.checklist.ChecklistApplicationBaseTest;
import dev.luisjohann.checklist.domain.project.IProjectRepository;
import dev.luisjohann.checklist.domain.project.Project;
import dev.luisjohann.checklist.domain.project.exceptions.ProjectNotFoundException;
import dev.luisjohann.checklist.domain.project.exceptions.WorkerAlreadyExistsException;
import dev.luisjohann.checklist.infra.project.controller.request.AddWorkerToProjectRequest;
import dev.luisjohann.checklist.infra.project.controller.response.AddWorkerToProjectResponse;

public class AddWorkerToProjectControllerTest extends ChecklistApplicationBaseTest {
    private static final String URI = "/project/worker";
    private static final String VALID_SLUG_PROJECT = "existing_slug";
    private static final String VALID_SLUG_PROJECT_2 = "existing_slug_2";
    private static final String INVALID_SLUG_PROJECT = "not_existing_slug";

    @Autowired
    private IProjectRepository projectRepository;

    @BeforeAll
    public void start() {
        projectRepository.createProject(new Project(VALID_SLUG_PROJECT, VALID_SLUG_PROJECT, null));
        projectRepository.createProject(new Project(VALID_SLUG_PROJECT_2, VALID_SLUG_PROJECT_2, null));
    }

    @Test
    void testAddWorkerWithInvalidSlugProject_thenRetrieveError() {
        var workerWithInvalidProjectSlug = buildWorker("Worker Name", INVALID_SLUG_PROJECT);

        webTestClient
                .post()
                .uri(URI)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(workerWithInvalidProjectSlug)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(ProjectNotFoundException.class);
    }

    @Test
    void testAddWorkersWithSameNameInSameProject_thenRetriveWorkerExists() {
        var workerWithValidProjectSlug = buildWorker("Duplicate Worker Name", VALID_SLUG_PROJECT);

        webTestClient
                .post()
                .uri(URI)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(workerWithValidProjectSlug)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(AddWorkerToProjectResponse.class)
                .value(resp -> {
                    assertEquals(resp.workerName(), workerWithValidProjectSlug.workerName());
                });

        webTestClient
                .post()
                .uri(URI)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(workerWithValidProjectSlug)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(WorkerAlreadyExistsException.class);
    }

    @Test
    void shouldAddWorkersWithSameNameInDiferentProjects() {
        var workerName = "Duplicate Worker Dif Project";
        var workerWithValidProjectSlug = buildWorker(workerName, VALID_SLUG_PROJECT);
        var workerWithValidProjectSlug2 = buildWorker(workerName, VALID_SLUG_PROJECT_2);

        webTestClient
                .post()
                .uri(URI)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(workerWithValidProjectSlug)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(AddWorkerToProjectResponse.class)
                .value(resp -> {
                    assertEquals(resp.workerName(), workerWithValidProjectSlug.workerName());
                });

        webTestClient
                .post()
                .uri(URI)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(workerWithValidProjectSlug2)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(AddWorkerToProjectResponse.class)
                .value(resp -> {
                    assertEquals(resp.workerName(), workerWithValidProjectSlug2.workerName());
                });
    }

    @Test
    void shouldAddWorkerWithValidSlugProject() {
        var workerWithValidProjectSlug = buildWorker("Correct Worker Name", VALID_SLUG_PROJECT);

        webTestClient
                .post()
                .uri(URI)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(workerWithValidProjectSlug)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(AddWorkerToProjectResponse.class)
                .value(resp -> {
                    assertEquals(resp.workerName(), workerWithValidProjectSlug.workerName());
                });
    }

    AddWorkerToProjectRequest buildWorker(String workerName, String projectName) {
        return new AddWorkerToProjectRequest(workerName, projectName);
    }
}
