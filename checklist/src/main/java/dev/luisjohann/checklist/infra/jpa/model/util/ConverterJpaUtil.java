package dev.luisjohann.checklist.infra.jpa.model.util;

import java.util.Objects;

import dev.luisjohann.checklist.domain.project.Project;
import dev.luisjohann.checklist.domain.project.Worker;
import dev.luisjohann.checklist.domain.todo.Todo;
import dev.luisjohann.checklist.infra.project.repository.jpa.model.ProjectJpaModel;
import dev.luisjohann.checklist.infra.project.repository.jpa.model.WorkerJpaModel;
import dev.luisjohann.checklist.infra.todo.repository.jpa.model.TodoJpaModel;

public class ConverterJpaUtil {

    public static ProjectJpaModel convertFromRecord(Project rec) {
        return Objects.isNull(rec) ? null : new ProjectJpaModel(rec.id(), rec.slug(), rec.name(), rec.description());
    }

    public static Project convertToRecord(ProjectJpaModel model) {
        return Objects.isNull(model) ? null
                : new Project(model.getId(), model.getSlug(), model.getName(), model.getDescription());
    }

    public static WorkerJpaModel convertFromRecord(Worker rec) {
        return Objects.isNull(rec) ? null
                : new WorkerJpaModel(rec.id(), rec.slug(), rec.name(),
                        ConverterJpaUtil.convertFromRecord(rec.project()));
    }

    public static Worker convertToRecord(WorkerJpaModel model) {
        return Objects.isNull(model) ? null
                : new Worker(model.getId(), model.getSlug(), model.getName(),
                        ConverterJpaUtil.convertToRecord(model.getProject()));
    }

    public static TodoJpaModel convertFromRecord(Todo rec) {
        return Objects.isNull(rec) ? null
                : new TodoJpaModel(rec.id(), rec.title(), rec.description(),
                        ConverterJpaUtil.convertFromRecord(rec.project()),
                        ConverterJpaUtil.convertFromRecord(rec.assignedTo()),
                        rec.createdAt(), rec.updatedAt(), rec.checkedAt(),
                        ConverterJpaUtil.convertFromRecord(rec.checkedWorker()));
    }

    public static Todo convertToRecord(TodoJpaModel model) {
        return Objects.isNull(model) ? null
                : new Todo(model.getId(), model.getTitle(), model.getDescription(),
                        ConverterJpaUtil.convertToRecord(model.getProject()),
                        ConverterJpaUtil.convertToRecord(model.getAssignedTo()),
                        model.getCreatedAt(), model.getUpdatedAt(), model.getCheckedAt(),
                        ConverterJpaUtil.convertToRecord(model.getCheckedWorker()));
    }

}
