package dev.luisjohann.checklist.domain.project;

import dev.luisjohann.checklist.domain.project.exceptions.ProjectNotFoundException;
import reactor.core.publisher.Mono;

public interface IProjectRepository {

    Mono<Project> createProject(Project project);

    Mono<Project> findBySlug(String slug) throws ProjectNotFoundException;
}
