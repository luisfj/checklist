package dev.luisjohann.checklist.infra.project.controller;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.stream.Stream;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import dev.luisjohann.checklist.ChecklistApplicationBaseTest;
import dev.luisjohann.checklist.domain.project.IProjectRepository;
import dev.luisjohann.checklist.domain.project.Project;
import dev.luisjohann.checklist.helper.MockProjectHelper;
import dev.luisjohann.checklist.infra.project.controller.response.FindAllProjectsResponse;

public class ListAllProjectsControllerTest extends ChecklistApplicationBaseTest {
    private static final String BASE_URI = "/project";

    @Autowired
    IProjectRepository repository;

    Project project;

    void createOneMockProject() {
        project = MockProjectHelper.createBean(repository);
    }

    @Test
    void testListAllProjects() {

        createOneMockProject();

        webTestClient
                .get()
                .uri(BASE_URI)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(FindAllProjectsResponse[].class).value(resp -> {
                    Assertions.assertThat(resp.length).isGreaterThan(0);
                    assertTrue(
                            Stream.of(resp).anyMatch(r -> project.getSlug().equals(r.slug())
                                    && project.getName().equals(r.name())
                                    && project.getDescription().equals(r.description())));
                });
    }

}