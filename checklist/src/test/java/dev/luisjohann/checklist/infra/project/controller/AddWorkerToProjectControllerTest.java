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
import dev.luisjohann.checklist.helper.MockProjectHelper;
import dev.luisjohann.checklist.infra.project.controller.request.AddWorkerToProjectRequest;
import dev.luisjohann.checklist.infra.project.controller.response.AddWorkerToProjectResponse;

public class AddWorkerToProjectControllerTest extends ChecklistApplicationBaseTest {
    private static final String URI = "/worker";

    @Autowired
    private IProjectRepository projectRepository;

    private Project project;
    private Project otherProject;
    private Project invalidProject;

    @BeforeAll
    public void start() {
        project = MockProjectHelper.createBean(projectRepository);
        otherProject = MockProjectHelper.createBean(projectRepository);
        invalidProject = MockProjectHelper.createNotPersistedBean();
    }

    @Test
    void testAddWorkerWithInvalidSlugProject_thenRetrieveError() {
        var workerWithInvalidProjectSlug = buildWorker("Worker Name", invalidProject.slug());

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
        var workerWithValidProjectSlug = buildWorker("Duplicate Worker Name", project.slug());

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
        var workerWithValidProjectSlug = buildWorker(workerName, project.slug());
        var workerWithValidProjectSlug2 = buildWorker(workerName, otherProject.slug());

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
        var workerWithValidProjectSlug = buildWorker("Correct Worker Name", project.slug());

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
