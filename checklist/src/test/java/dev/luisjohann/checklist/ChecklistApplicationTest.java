package dev.luisjohann.checklist;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;

public class ChecklistApplicationTest extends ChecklistApplicationBaseTest {

    @Value("${app.name}")
    private String appName;

    @Test
    void testLoadTestProperties() {
        assertEquals("Todo App Tests", appName);
    }

    @Test
    void testApplicationAvailable() {
        webTestClient
                .get()
                .uri("/")
                .accept(MediaType.ALL)
                .exchange()
                .expectStatus().isNotFound();
    }
}
