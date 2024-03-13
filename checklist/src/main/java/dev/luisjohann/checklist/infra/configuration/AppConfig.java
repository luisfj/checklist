package dev.luisjohann.checklist.infra.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.config.EnableWebFlux;

import dev.luisjohann.checklist.domain.project.IProjectRepository;
import dev.luisjohann.checklist.domain.project.ISlugGenerator;
import dev.luisjohann.checklist.domain.project.IWorkerRepository;
import dev.luisjohann.checklist.domain.todo.ICommentRepository;
import dev.luisjohann.checklist.domain.todo.ITodoRepository;
import dev.luisjohann.checklist.infra.project.ProjectRepositoryInMemory;
import dev.luisjohann.checklist.infra.project.SlugGeneratorTest;
import dev.luisjohann.checklist.infra.project.WorkerRepositoryInMemory;
import dev.luisjohann.checklist.infra.todo.CommentRepositoryInMemory;
import dev.luisjohann.checklist.infra.todo.TodoRepositoryInMemory;

@Configuration
@EnableWebFlux
public class AppConfig {
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
    public ITodoRepository todoRepository() {
        return new TodoRepositoryInMemory();
    }

    @Bean
    public ICommentRepository commentRepository() {
        return new CommentRepositoryInMemory();
    }

}
