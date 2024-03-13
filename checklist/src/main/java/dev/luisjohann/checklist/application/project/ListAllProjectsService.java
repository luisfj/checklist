package dev.luisjohann.checklist.application.project;

import org.springframework.stereotype.Service;

import dev.luisjohann.checklist.domain.project.IProjectRepository;
import dev.luisjohann.checklist.domain.project.Project;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;

@Service
@RequiredArgsConstructor
public class ListAllProjectsService {

    private final IProjectRepository projectRepository;

    public Flux<Project> listAllProjects() {
        return projectRepository.findAll();
    }
}
