package dev.luisjohann.checklist.application.project;

import java.util.Objects;
import java.util.concurrent.ExecutionException;

import org.springframework.stereotype.Service;

import dev.luisjohann.checklist.application.project.dto.AddWorkerToProjectDto;
import dev.luisjohann.checklist.domain.project.IProjectRepository;
import dev.luisjohann.checklist.domain.project.ISlugGenerator;
import dev.luisjohann.checklist.domain.project.IWorkerRepository;
import dev.luisjohann.checklist.domain.project.Project;
import dev.luisjohann.checklist.domain.project.Worker;
import dev.luisjohann.checklist.domain.project.exceptions.ProjectNotFoundException;
import dev.luisjohann.checklist.domain.project.exceptions.WorkerAlreadyExistsException;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class AddWorkerToProjectService {

    final IWorkerRepository workerRepository;
    final IProjectRepository projectRepository;
    final ISlugGenerator slugGenerator;

    public Mono<Worker> addWorkerToProject(AddWorkerToProjectDto dto) {
        Project p;
        try {
            p = projectRepository.findBySlug(dto.projectSlug()).toFuture().get();
            if (Objects.isNull(p)) {
                throw new ProjectNotFoundException(dto.projectSlug());
            }
            var workerExists = workerRepository.findByNameAndProjectSlug(dto.workerName(), dto.projectSlug())
                    .toFuture().get();
            if (Objects.nonNull(workerExists)) {
                throw new WorkerAlreadyExistsException(dto.workerName(), dto.projectSlug());
            }
            Worker worker = buildWorker(p, dto);
            return workerRepository.createWorker(worker);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return Mono.error(new RuntimeException());
    }

    private Worker buildWorker(Project project, AddWorkerToProjectDto dto) {
        return new Worker(null, slugGenerator.generateSlugy(dto.workerName()), dto.workerName(), project);
    }
}
