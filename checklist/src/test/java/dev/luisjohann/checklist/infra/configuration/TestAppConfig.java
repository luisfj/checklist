package dev.luisjohann.checklist.infra.configuration;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.config.EnableWebFlux;

import dev.luisjohann.checklist.domain.project.IProjectRepository;
import dev.luisjohann.checklist.domain.project.ISlugGenerator;
import dev.luisjohann.checklist.domain.project.IWorkerRepository;
import dev.luisjohann.checklist.domain.todo.ICommentRepository;
import dev.luisjohann.checklist.domain.todo.ITodoRepository;
import dev.luisjohann.checklist.infra.project.repository.memory.ProjectRepositoryInMemory;
import dev.luisjohann.checklist.infra.project.repository.memory.WorkerRepositoryInMemory;
import dev.luisjohann.checklist.infra.project.slug.memory.SlugGeneratorTest;
import dev.luisjohann.checklist.infra.todo.repository.memory.CommentRepositoryInMemory;
import dev.luisjohann.checklist.infra.todo.repository.memory.TodoRepositoryInMemory;

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

    @Bean
    public ICommentRepository commentRepository() {
        return new CommentRepositoryInMemory();
    }

    @Bean
    public ITodoRepository todoRepository() {
        return new TodoRepositoryInMemory();
    }
}
