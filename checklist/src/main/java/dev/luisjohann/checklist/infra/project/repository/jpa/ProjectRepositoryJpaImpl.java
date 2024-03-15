package dev.luisjohann.checklist.infra.project.repository.jpa;

import org.springframework.data.repository.NoRepositoryBean;

import dev.luisjohann.checklist.domain.project.IProjectRepository;
import dev.luisjohann.checklist.domain.project.Project;
import dev.luisjohann.checklist.infra.project.repository.jpa.model.ConverterJpaUtil;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@NoRepositoryBean
@RequiredArgsConstructor
public class ProjectRepositoryJpaImpl implements IProjectRepository {

    final ProjectRepositoryJpa repositoryJpa;

    @Override
    public Mono<Project> createProject(Project project) {
        var cProject = ConverterJpaUtil.convertRecordToProject(project);
        var ret = ConverterJpaUtil.convertWokerToRecord(repositoryJpa.save(cProject));
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

}
