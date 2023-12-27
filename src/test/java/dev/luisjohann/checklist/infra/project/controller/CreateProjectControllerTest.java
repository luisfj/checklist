package dev.luisjohann.checklist.infra.project.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import dev.luisjohann.checklist.ChecklistApplicationBaseTest;
import dev.luisjohann.checklist.infra.project.controller.request.CreateProjectRequest;
import dev.luisjohann.checklist.infra.project.controller.response.CreateProjectResponse;

public class CreateProjectControllerTest extends ChecklistApplicationBaseTest {
    private static final String URI = "/project/create";

    @Test
    void createOneProject_thenReturnProject() {

        CreateProjectRequest createRequestObj = new CreateProjectRequest("Test on Create", "Testing create end point");

        webTestClient
                .post()
                .uri(URI)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(createRequestObj)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(CreateProjectResponse.class)
                .value(resp -> {
                    assertEquals(resp.slug(), "test_on_create");
                    assertEquals(resp.name(), "Test on Create");
                    assertEquals(resp.description(), "Testing create end point");
                });
    }

    @Test
    void createTwoEqualsProject_thenSaveWithDiferentSlugs() {

        CreateProjectRequest createRequestObj = new CreateProjectRequest("Test on Create 2",
                "Testing create 2 end point");

        webTestClient
                .post()
                .uri(URI)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(createRequestObj)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(CreateProjectResponse.class)
                .value(resp -> {
                    assertEquals(resp.slug(), "test_on_create_2");
                    assertEquals(resp.name(), createRequestObj.name());
                    assertEquals(resp.description(), createRequestObj.description());
                });

        webTestClient
                .post()
                .uri(URI)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(createRequestObj)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(CreateProjectResponse.class)
                .value(resp -> {
                    assertNotEquals(resp.slug(), "test_on_create_2");
                    assertTrue(resp.slug().startsWith("test_on_create_2"));
                    assertEquals(resp.name(), createRequestObj.name());
                    assertEquals(resp.description(), createRequestObj.description());
                });
    }

}
