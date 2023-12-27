package dev.luisjohann.checklist.application.project;

import org.springframework.stereotype.Service;

import dev.luisjohann.checklist.domain.project.IProjectRepository;
import dev.luisjohann.checklist.domain.project.Project;
import dev.luisjohann.checklist.domain.project.exceptions.ProjectNotFoundException;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class FindBySlugProjectService {

    private final IProjectRepository projectRepository;

    public Mono<Project> findBySlug(String slug) {
        return projectRepository.findBySlug(slug).switchIfEmpty(Mono.error(new ProjectNotFoundException(slug)));
    }
}
