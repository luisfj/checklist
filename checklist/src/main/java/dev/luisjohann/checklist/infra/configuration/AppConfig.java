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
import dev.luisjohann.checklist.infra.project.repository.jpa.WorkerRepositoryJpa;
import dev.luisjohann.checklist.infra.project.repository.jpa.WorkerRepositoryJpaImpl;
import dev.luisjohann.checklist.infra.project.slug.slugify.SlugifyGenerator;
import dev.luisjohann.checklist.infra.todo.repository.jpa.CommentRepositoryJpa;
import dev.luisjohann.checklist.infra.todo.repository.jpa.CommentRepositoryJpaImpl;
import dev.luisjohann.checklist.infra.todo.repository.jpa.TodoRepositoryJpa;
import dev.luisjohann.checklist.infra.todo.repository.jpa.TodoRepositoryJpaImpl;

@Configuration
@EnableWebFlux
@EnableJpaRepositories(basePackages = {
        "dev.luisjohann.checklist.infra.project.repository.jpa",
        "dev.luisjohann.checklist.infra.todo.repository.jpa"
})
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
    public IWorkerRepository workerRepository(final WorkerRepositoryJpa repoJpa) {
        return new WorkerRepositoryJpaImpl(repoJpa);
    }

    @Bean
    public ITodoRepository todoRepository(final TodoRepositoryJpa repoJpa) {
        return new TodoRepositoryJpaImpl(repoJpa);
    }

    @Bean
    public ICommentRepository commentRepository(final CommentRepositoryJpa repoJpa) {
        return new CommentRepositoryJpaImpl(repoJpa);
    }

}
