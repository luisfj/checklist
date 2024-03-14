package dev.luisjohann.checklist.infra.project.repository.jpa;

import org.springframework.data.repository.NoRepositoryBean;

import dev.luisjohann.checklist.domain.project.IProjectRepository;
import dev.luisjohann.checklist.domain.project.Project;
import dev.luisjohann.checklist.infra.project.repository.jpa.model.ProjectJpaModel;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@NoRepositoryBean
@RequiredArgsConstructor
public class ProjectRepositoryJpaImpl implements IProjectRepository {

    final ProjectRepositoryJpa repositoryJpa;

    @Override
    public Mono<Project> createProject(Project project) {
        var cProject = convertToJpaModel(project);
        var ret = convertToModel(repositoryJpa.save(cProject));
        return Mono.just(ret);
    }

    @Override
    public Mono<Project> findBySlug(String slug) {
        var optEntity = repositoryJpa.findBySlug(slug);
        if (optEntity.isPresent()) {
            return Mono.just(optEntity.get());
        }

        return Mono.empty();
    }

    @Override
    public Flux<Project> findAll() {
        return Flux.fromIterable(repositoryJpa.findAllByOrderByNameAsc());
    }

    ProjectJpaModel convertToJpaModel(Project project) {
        return new ProjectJpaModel(project.id(), project.slug(), project.name(), project.description());
    }

    Project convertToModel(ProjectJpaModel project) {
        return new Project(project.getId(), project.getSlug(), project.getName(), project.getDescription());
    }

}
