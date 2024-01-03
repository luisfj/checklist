package dev.luisjohann.checklist.domain.project;

import reactor.core.publisher.Mono;

public interface IProjectRepository {

    Mono<Project> createProject(Project project);

    Mono<Project> findBySlug(String slug);
}
