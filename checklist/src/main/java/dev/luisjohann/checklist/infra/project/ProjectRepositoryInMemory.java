package dev.luisjohann.checklist.infra.project;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import dev.luisjohann.checklist.domain.project.IProjectRepository;
import dev.luisjohann.checklist.domain.project.Project;
import dev.luisjohann.checklist.domain.project.exceptions.ProjectWithSameSlugException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class ProjectRepositoryInMemory implements IProjectRepository {

    private List<Project> projects = new ArrayList<>();

    @Override
    public Mono<Project> createProject(Project project) {
        if (this.projects.stream().anyMatch(p -> p.getSlug().equals(project.getSlug()))) {
            return Mono.error(new ProjectWithSameSlugException(project.getSlug()));
        }
        this.projects.add(project);
        return Mono.just(project);
    }

    @Override
    public Mono<Project> findBySlug(String slug) {
        var ret = this.projects.stream().filter(p -> p.getSlug().equals(slug)).findFirst().orElse(null);

        return Objects.isNull(ret) ? Mono.empty() : Mono.just(ret);
    }

    @Override
    public Flux<Project> findAll() {
        return Flux.fromIterable(this.projects);
    }

}
