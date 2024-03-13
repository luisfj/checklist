package dev.luisjohann.checklist.application.project;

import java.util.Objects;

import org.springframework.stereotype.Service;

import dev.luisjohann.checklist.domain.project.IProjectRepository;
import dev.luisjohann.checklist.domain.project.IWorkerRepository;
import dev.luisjohann.checklist.domain.project.Worker;
import dev.luisjohann.checklist.domain.project.exceptions.ProjectNotFoundException;
import dev.luisjohann.checklist.domain.project.exceptions.WorkersNotFoundForTheProjectException;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;

@Service
@RequiredArgsConstructor
public class ListWorkersFromProjectBySlugService {

    private final IWorkerRepository workerRepository;
    private final IProjectRepository projectRepository;

    public Flux<Worker> findWorkersByProjectSlug(String slug) {
        try {
            if (Objects.isNull(projectRepository.findBySlug(slug).toFuture().get())) {
                return Flux.error(new ProjectNotFoundException(slug));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Flux.error(new RuntimeException(e.getMessage()));
        }
        return workerRepository.findByProjectSlug(slug)
                .switchIfEmpty(Flux.error(new WorkersNotFoundForTheProjectException(slug)));
    }
}
