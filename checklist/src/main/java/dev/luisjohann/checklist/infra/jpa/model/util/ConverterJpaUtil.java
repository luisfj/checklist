package dev.luisjohann.checklist.infra.jpa.model.util;

import java.util.Objects;

import dev.luisjohann.checklist.domain.project.Project;
import dev.luisjohann.checklist.domain.project.Worker;
import dev.luisjohann.checklist.domain.todo.Todo;
import dev.luisjohann.checklist.infra.project.repository.jpa.model.ProjectJpaModel;
import dev.luisjohann.checklist.infra.project.repository.jpa.model.WorkerJpaModel;
import dev.luisjohann.checklist.infra.todo.repository.jpa.model.TodoJpaModel;

public class ConverterJpaUtil {

    public static ProjectJpaModel convertRecordToProject(Project rec) {
        return Objects.isNull(rec) ? null : new ProjectJpaModel(rec.id(), rec.slug(), rec.name(), rec.description());
    }

    public static Project convertProjectToRecord(ProjectJpaModel model) {
        return Objects.isNull(model) ? null
                : new Project(model.getId(), model.getSlug(), model.getName(), model.getDescription());
    }

    public static WorkerJpaModel convertRecordToWorker(Worker rec) {
        return Objects.isNull(rec) ? null
                : new WorkerJpaModel(rec.id(), rec.slug(), rec.name(),
                        ConverterJpaUtil.convertRecordToProject(rec.project()));
    }

    public static Worker convertWorkerToRecord(WorkerJpaModel model) {
        return Objects.isNull(model) ? null
                : new Worker(model.getId(), model.getSlug(), model.getName(),
                        ConverterJpaUtil.convertProjectToRecord(model.getProject()));
    }

    public static TodoJpaModel convertRecordToTodo(Todo rec) {
        return Objects.isNull(rec) ? null
                : new TodoJpaModel(rec.id(), rec.title(), rec.description(),
                        ConverterJpaUtil.convertRecordToProject(rec.project()),
                        ConverterJpaUtil.convertRecordToWorker(rec.assignedTo()),
                        rec.createdAt(), rec.updatedAt(), rec.checkedAt(),
                        ConverterJpaUtil.convertRecordToWorker(rec.checkedWorker()));
    }

    public static Todo convertTodoToRecord(TodoJpaModel model) {
        return Objects.isNull(model) ? null
                : new Todo(model.getId(), model.getTitle(), model.getDescription(),
                        ConverterJpaUtil.convertProjectToRecord(model.getProject()),
                        ConverterJpaUtil.convertWorkerToRecord(model.getAssignedTo()),
                        model.getCreatedAt(), model.getUpdatedAt(), model.getCheckedAt(),
                        ConverterJpaUtil.convertWorkerToRecord(model.getCheckedWorker()));
    }

}
