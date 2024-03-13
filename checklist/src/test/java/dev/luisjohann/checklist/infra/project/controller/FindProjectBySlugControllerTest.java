package dev.luisjohann.checklist.infra.project.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import dev.luisjohann.checklist.ChecklistApplicationBaseTest;
import dev.luisjohann.checklist.domain.project.IProjectRepository;
import dev.luisjohann.checklist.domain.project.Project;
import dev.luisjohann.checklist.helper.MockProjectHelper;
import dev.luisjohann.checklist.infra.project.controller.response.FindProjectBySlugResponse;

public class FindProjectBySlugControllerTest extends ChecklistApplicationBaseTest {
    private static final String BASE_URI = "/project/";

    @Autowired
    IProjectRepository repository;

    Project project;
    Project notExistsProject;

    @BeforeAll
    void createOneMockProject() {
        project = MockProjectHelper.createBean(repository);
        notExistsProject = MockProjectHelper.createNotPersistedBean();
    }

    @Test
    void getByExistingSlug_thenReturnProject() {
        webTestClient
                .get()
                .uri(BASE_URI + project.getSlug())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(FindProjectBySlugResponse.class).value(resp -> {
                    assertEquals(resp.slug(), project.getSlug());
                    assertEquals(resp.name(), project.getName());
                    assertEquals(resp.description(), project.getDescription());
                });
    }

    @Test
    void getByNotExistingSlug_thenReturnStatusNotFound() {
        webTestClient
                .get()
                .uri(BASE_URI + notExistsProject.getSlug())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNotFound();
    }
}
