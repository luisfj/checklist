package dev.luisjohann.checklist.infra.project.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import dev.luisjohann.checklist.ChecklistApplicationBaseTest;
import dev.luisjohann.checklist.domain.project.IProjectRepository;
import dev.luisjohann.checklist.domain.project.Project;
import dev.luisjohann.checklist.infra.project.controller.response.FindProjectBySlugResponse;

public class FindProjectBySlugControllerTest extends ChecklistApplicationBaseTest {
    private static final String BASE_URI = "/project/";

    @Autowired
    IProjectRepository repository;

    Project mockObject;

    void createOneMockProject() {
        mockObject = new Project("teste_find_slug", "Test Find", "Testing find end point");
        repository.createProject(mockObject);
    }

    @Test
    void getByExistingSlug_thenReturnProject() {
        createOneMockProject();

        webTestClient
                .get()
                .uri(BASE_URI + mockObject.getSlug())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(FindProjectBySlugResponse.class).value(resp -> {
                    assertEquals(resp.slug(), mockObject.getSlug());
                    assertEquals(resp.name(), mockObject.getName());
                    assertEquals(resp.description(), mockObject.getDescription());
                });
    }

    @Test
    void getByNotExistingSlug_thenReturnStatusNotFound() {
        webTestClient
                .get()
                .uri(BASE_URI + "not_exists_slug")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNotFound();
    }
}
