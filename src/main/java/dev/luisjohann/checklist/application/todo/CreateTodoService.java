package dev.luisjohann.checklist.application.todo;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Service;

import dev.luisjohann.checklist.application.todo.dto.CreateTodoDto;
import dev.luisjohann.checklist.domain.project.IProjectRepository;
import dev.luisjohann.checklist.domain.project.IWorkerRepository;
import dev.luisjohann.checklist.domain.project.Project;
import dev.luisjohann.checklist.domain.project.Worker;
import dev.luisjohann.checklist.domain.project.exceptions.ProjectNotFoundException;
import dev.luisjohann.checklist.domain.project.exceptions.WorkerWithSlugNotFoundException;
import dev.luisjohann.checklist.domain.todo.ITodoRepository;
import dev.luisjohann.checklist.domain.todo.Todo;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class CreateTodoService {

    final IWorkerRepository workerRepository;
    final IProjectRepository projectRepository;
    final ITodoRepository todoRepository;

    public Mono<Todo> createTodo(CreateTodoDto dto) {
        try {
            Project p = projectRepository.findBySlug(dto.projectSlug()).toFuture().get();
            Worker workerAssigned = null;
            if (Objects.isNull(p)) {
                throw new ProjectNotFoundException(dto.projectSlug());
            }
            if (Strings.isNotBlank(dto.workerAssignedSlug())) {
                workerAssigned = workerRepository
                        .findBySlugAndProjectSlug(dto.workerAssignedSlug(), dto.projectSlug())
                        .toFuture().get();
                if (Objects.isNull(workerAssigned)) {
                    throw new WorkerWithSlugNotFoundException(dto.workerAssignedSlug(), dto.projectSlug());
                }
            }

            Todo newTodo = buildTodo(p, workerAssigned, dto);
            return todoRepository.createTodo(newTodo);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return Mono.error(new RuntimeException());
    }

    private Todo buildTodo(Project project, Worker workerAssignedTo, CreateTodoDto dto) {
        return new Todo(UUID.randomUUID().toString(), dto.title(), dto.description(), project, workerAssignedTo,
                LocalDateTime.now(), null, null, null);
    }
}
