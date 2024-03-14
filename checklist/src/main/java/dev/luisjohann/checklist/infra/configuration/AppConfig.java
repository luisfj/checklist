package dev.luisjohann.checklist.infra.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.reactive.config.EnableWebFlux;

import dev.luisjohann.checklist.domain.project.IProjectRepository;
import dev.luisjohann.checklist.domain.project.ISlugGenerator;
import dev.luisjohann.checklist.domain.project.IWorkerRepository;
import dev.luisjohann.checklist.domain.todo.ICommentRepository;
import dev.luisjohann.checklist.domain.todo.ITodoRepository;
import dev.luisjohann.checklist.infra.project.repository.jpa.ProjectRepositoryJpa;
import dev.luisjohann.checklist.infra.project.repository.jpa.ProjectRepositoryJpaImpl;
import dev.luisjohann.checklist.infra.project.repository.memory.WorkerRepositoryInMemory;
import dev.luisjohann.checklist.infra.project.slug.slugify.SlugifyGenerator;
import dev.luisjohann.checklist.infra.todo.CommentRepositoryInMemory;
import dev.luisjohann.checklist.infra.todo.TodoRepositoryInMemory;

@Configuration
@EnableWebFlux
@EnableJpaRepositories(basePackages = "dev.luisjohann.checklist.infra.project.repository.jpa")
@EnableTransactionManagement
public class AppConfig {

    @Bean
    public IProjectRepository projectRepository(final ProjectRepositoryJpa repoJpa) {
        return new ProjectRepositoryJpaImpl(repoJpa);
    }

    @Bean
    public ISlugGenerator slugGenerator() {
        return new SlugifyGenerator();
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
