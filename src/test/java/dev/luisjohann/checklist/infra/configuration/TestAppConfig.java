package dev.luisjohann.checklist.infra.configuration;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.config.EnableWebFlux;

import dev.luisjohann.checklist.domain.project.IProjectRepository;
import dev.luisjohann.checklist.domain.project.ISlugGenerator;
import dev.luisjohann.checklist.domain.project.IWorkerRepository;
import dev.luisjohann.checklist.infra.project.ProjectRepositoryInMemory;
import dev.luisjohann.checklist.infra.project.SlugGeneratorTest;
import dev.luisjohann.checklist.infra.project.WorkerRepositoryInMemory;

@TestConfiguration
@EnableWebFlux
public class TestAppConfig {

    @Bean
    public IProjectRepository projectRepository() {
        return new ProjectRepositoryInMemory();
    }

    @Bean
    public ISlugGenerator slugGenerator() {
        return new SlugGeneratorTest();
    }

    @Bean
    public IWorkerRepository workerRepository() {
        return new WorkerRepositoryInMemory();
    }

}
