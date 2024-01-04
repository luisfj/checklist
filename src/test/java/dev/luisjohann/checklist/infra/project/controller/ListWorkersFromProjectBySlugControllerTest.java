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
import dev.luisjohann.checklist.helper.MockProjectHelper;
import dev.luisjohann.checklist.helper.MockWorkerHelper;
import dev.luisjohann.checklist.infra.project.controller.response.WorkersFromProjectResponse;

public class ListWorkersFromProjectBySlugControllerTest extends ChecklistApplicationBaseTest {
        private static final String BASE_URI = "/worker/";

        @Autowired
        private IProjectRepository projectRepository;
        @Autowired
        private IWorkerRepository workerRepository;

        private List<WorkersFromProjectResponse> responseExpected = new ArrayList<>();

        private Project projectWithWorkers;
        private Project projectWithoutWorkers;
        private Project projectInvalid;

        @BeforeAll
        void start() {
                projectWithWorkers = MockProjectHelper.createBean(projectRepository);
                projectWithoutWorkers = MockProjectHelper.createBean(projectRepository);
                projectInvalid = MockProjectHelper.createNotPersistedBean();

                Worker worker = MockWorkerHelper.createBean(workerRepository, projectWithWorkers);
                Worker worker2 = MockWorkerHelper.createOtherBean(workerRepository, projectWithWorkers);

                responseExpected.add(
                                new WorkersFromProjectResponse(worker2.getSlug(), worker2.getName()));
                responseExpected.add(
                                new WorkersFromProjectResponse(worker.getSlug(), worker.getName()));
        }

        @Test
        void testWithExistingProjectSlug_thenReturnWorkersFromProject() {
                webTestClient
                                .get()
                                .uri(BASE_URI + projectWithWorkers.getSlug())
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
                                .uri(BASE_URI + projectWithoutWorkers.getSlug())
                                .accept(MediaType.APPLICATION_JSON)
                                .exchange()
                                .expectStatus().isNotFound()
                                .expectBody(WorkerNotFoundException.class);
        }

        @Test
        void testWithNotExistingProjectSlug_thenReturnEmptyWorkers() {
                webTestClient
                                .get()
                                .uri(BASE_URI + projectInvalid.getSlug())
                                .accept(MediaType.APPLICATION_JSON)
                                .exchange()
                                .expectStatus().isNotFound()
                                .expectBody(ProjectNotFoundException.class);
        }

}
