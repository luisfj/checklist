package dev.luisjohann.checklist.application.project;

import java.util.Objects;
import java.util.concurrent.ExecutionException;

import org.springframework.stereotype.Service;

import dev.luisjohann.checklist.application.project.dto.RemoveWorkerFromProjectDto;
import dev.luisjohann.checklist.domain.project.IProjectRepository;
import dev.luisjohann.checklist.domain.project.IWorkerRepository;
import dev.luisjohann.checklist.domain.project.Project;
import dev.luisjohann.checklist.domain.project.exceptions.ProjectNotFoundException;
import dev.luisjohann.checklist.domain.project.exceptions.WorkerNotFoundException;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class RemoveWorkerFromProjectService {

    final IWorkerRepository workerRepository;
    final IProjectRepository projectRepository;

    public Mono<Void> removeWorkerFromProject(RemoveWorkerFromProjectDto dto) {
        Project p;
        try {
            p = projectRepository.findBySlug(dto.projectSlug()).toFuture().get();
            if (Objects.isNull(p)) {
                throw new ProjectNotFoundException(dto.projectSlug());
            }
            var worker = workerRepository.findBySlugAndProjectSlug(dto.workerSlug(), dto.projectSlug())
                    .toFuture().get();
            if (Objects.isNull(worker)) {
                throw new WorkerNotFoundException(dto.workerSlug(), dto.projectSlug());
            }

            return workerRepository.removeWorker(worker);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return Mono.error(new RuntimeException());
    }
}
