package dev.luisjohann.checklist.infra.project.controller;

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
import dev.luisjohann.checklist.domain.project.exceptions.WorkerNotFoundException;

public class RemoveWorkerFromProjectControllerTest extends ChecklistApplicationBaseTest {
    private static final String URI = "/project/worker/%s/%s";
    private static final String VALID_SLUG_PROJECT = "existing_slug";
    private static final String INVALID_SLUG_PROJECT = "existing_slug_not_exists";
    private static final String WORKER_SLUG = "worker_name";
    private static final String INVALID_WORKER_SLUG = "worker_name_not_exists";

    @Autowired
    private IProjectRepository projectRepository;
    @Autowired
    private IWorkerRepository workerRepository;
    private Project project;
    private Worker worker;

    @BeforeAll
    void start() {
        project = new Project(VALID_SLUG_PROJECT, VALID_SLUG_PROJECT, null);
        projectRepository.createProject(project);

    }

    @BeforeEach
    void createWorker() {
        worker = workerRepository.createWorker(new Worker(WORKER_SLUG, WORKER_SLUG, project)).block();
    }

    @AfterEach
    void removeWorker() {
        workerRepository.removeWorker(worker);
        worker = null;
    }

    @Test
    void testRemoveWorkerWithProjectNotExists_thenReturnProjectNotFoundException() {
        var requestUri = buildUri(WORKER_SLUG, INVALID_SLUG_PROJECT);

        webTestClient
                .delete()
                .uri(requestUri)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(ProjectNotFoundException.class);
    }

    @Test
    void testRemoveWorkerWithWorkerNameNotExists_thenReturnWorkerNotFoundException() {
        var requestUri = buildUri(INVALID_WORKER_SLUG, VALID_SLUG_PROJECT);

        webTestClient
                .delete()
                .uri(requestUri)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(WorkerNotFoundException.class);
    }

    @Test
    void shouldRemoveWorker() {
        assertNotNull(workerRepository.findBySlugAndProjectSlug(WORKER_SLUG, VALID_SLUG_PROJECT).block());

        var requestUri = buildUri(WORKER_SLUG, VALID_SLUG_PROJECT);

        webTestClient
                .delete()
                .uri(requestUri)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk();

        assertNull(workerRepository.findByNameAndProjectSlug(WORKER_SLUG, VALID_SLUG_PROJECT).block());
    }

    String buildUri(String workerSlug, String projectSlug) {
        return String.format(URI, projectSlug, workerSlug);
    }
}
