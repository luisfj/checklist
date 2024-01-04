package dev.luisjohann.checklist.infra.project.controller;

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
import dev.luisjohann.checklist.helper.MockProjectHelper;
import dev.luisjohann.checklist.helper.MockWorkerHelper;

public class RemoveWorkerFromProjectControllerTest extends ChecklistApplicationBaseTest {
    private static final String URI = "/worker/%s/%s";

    @Autowired
    private IProjectRepository projectRepository;
    @Autowired
    private IWorkerRepository workerRepository;
    private Project project;
    private Project projectInvalid;
    private Worker worker;
    private Worker workerInvalid;

    @BeforeAll
    void start() {
        project = MockProjectHelper.createBean(projectRepository);
        projectInvalid = MockProjectHelper.createNotPersistedBean();
    }

    @BeforeEach
    void createWorker() {
        worker = MockWorkerHelper.createBean(workerRepository, project);
        workerInvalid = MockWorkerHelper.createNotPersistBean(project);
    }

    @AfterEach
    void removeWorker() {
        workerRepository.removeWorker(worker);
        worker = null;
    }

    @Test
    void testRemoveWorkerWithProjectNotExists_thenReturnProjectNotFoundException() {
        var requestUri = buildUri(worker.getSlug(), projectInvalid.getSlug());

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
        var requestUri = buildUri(workerInvalid.getSlug(), project.getSlug());

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
        var requestUri = buildUri(worker.getSlug(), project.getSlug());

        webTestClient
                .delete()
                .uri(requestUri)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk();

        assertNull(workerRepository.findByNameAndProjectSlug(worker.getSlug(), project.getSlug()).block());
    }

    String buildUri(String workerSlug, String projectSlug) {
        return String.format(URI, projectSlug, workerSlug);
    }
}
