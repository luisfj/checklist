package dev.luisjohann.checklist;

import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;

import dev.luisjohann.checklist.infra.configuration.TestAppConfig;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = "spring.main.allow-bean-definition-overriding=true")
@TestPropertySource(locations = "classpath:test.properties")
@TestInstance(value = Lifecycle.PER_CLASS)
@ContextConfiguration(classes = TestAppConfig.class)
public abstract class ChecklistApplicationBaseTest {

    @Autowired
    protected WebTestClient webTestClient;

}
