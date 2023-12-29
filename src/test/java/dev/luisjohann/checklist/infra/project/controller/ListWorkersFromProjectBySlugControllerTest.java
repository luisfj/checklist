package dev.luisjohann.checklist.infra.project.controller;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.assertj.core.util.Arrays;
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
import dev.luisjohann.checklist.domain.project.exceptions.WorkerNotFoundException;
import dev.luisjohann.checklist.infra.project.controller.response.WorkersFromProjectResponse;

public class ListWorkersFromProjectBySlugControllerTest extends ChecklistApplicationBaseTest {
        private static final String BASE_URI = "/project/worker/";

        private static final String VALID_SLUG_PROJECT = "existing_slug";
        private static final String VALID_SLUG_PROJECT_NO_WORKERS = "existing_slug_no_workers";
        private static final String INVALID_SLUG_PROJECT = "existing_slug_not_exists";
        private static final String WORKER_SLUG = "worker_name";
        private static final String WORKER_SLUG_OTHER = "worker_name_other";

        @Autowired
        private IProjectRepository projectRepository;
        @Autowired
        private IWorkerRepository workerRepository;

        private List<WorkersFromProjectResponse> responseExpected = new ArrayList<>();

        @BeforeAll
        void start() {
                Project project = new Project(VALID_SLUG_PROJECT, VALID_SLUG_PROJECT, null);
                Project project2 = new Project(VALID_SLUG_PROJECT_NO_WORKERS, VALID_SLUG_PROJECT_NO_WORKERS, null);
                projectRepository.createProject(project);
                projectRepository.createProject(project2);

                Worker worker = workerRepository.createWorker(new Worker(WORKER_SLUG, WORKER_SLUG, project)).block();
                Worker worker2 = workerRepository
                                .createWorker(new Worker(WORKER_SLUG_OTHER, WORKER_SLUG_OTHER, project))
                                .block();

                responseExpected.add(
                                new WorkersFromProjectResponse(worker2.getSlug(), worker2.getName()));
                responseExpected.add(
                                new WorkersFromProjectResponse(worker.getSlug(), worker.getName()));
        }

        @Test
        void testWithExistingProjectSlug_thenReturnWorkersFromProject() {
                webTestClient
                                .get()
                                .uri(BASE_URI + VALID_SLUG_PROJECT)
                                .accept(MediaType.APPLICATION_JSON)
                                .exchange()
                                .expectStatus().isOk()
                                .expectBody(WorkersFromProjectResponse[].class).value(list -> {
                                        assertTrue(responseExpected.size() > 0 && Objects.nonNull(list)
                                                        && Arrays.asList(list).containsAll(responseExpected));
                                });
        }

        @Test
        void testWithExistingProjectSlugWhereNotHaveWorkers_thenReturnWorkerNotFoundException() {
                webTestClient
                                .get()
                                .uri(BASE_URI + VALID_SLUG_PROJECT_NO_WORKERS)
                                .accept(MediaType.APPLICATION_JSON)
                                .exchange()
                                .expectStatus().isNotFound()
                                .expectBody(WorkerNotFoundException.class);
        }

        @Test
        void testWithNotExistingProjectSlug_thenReturnEmptyWorkers() {
                webTestClient
                                .get()
                                .uri(BASE_URI + INVALID_SLUG_PROJECT)
                                .accept(MediaType.APPLICATION_JSON)
                                .exchange()
                                .expectStatus().isNotFound()
                                .expectBody(ProjectNotFoundException.class);
        }

}
