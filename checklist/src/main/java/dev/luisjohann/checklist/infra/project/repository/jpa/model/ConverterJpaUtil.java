package dev.luisjohann.checklist.infra.project.repository.jpa.model;

import dev.luisjohann.checklist.domain.project.Project;
import dev.luisjohann.checklist.domain.project.Worker;

public class ConverterJpaUtil {

    public static ProjectJpaModel convertRecordToProject(Project rec) {
        return new ProjectJpaModel(rec.id(), rec.slug(), rec.name(), rec.description());
    }

    public static Project convertWokerToRecord(ProjectJpaModel model) {
        return new Project(model.getId(), model.getSlug(), model.getName(), model.getDescription());
    }

    public static WorkerJpaModel convertRecordToWorker(Worker rec) {
        return new WorkerJpaModel(rec.id(), rec.slug(), rec.name(),
                ConverterJpaUtil.convertRecordToProject(rec.project()));
    }

    public static Worker convertWokerToRecord(WorkerJpaModel project) {
        return new Worker(project.getId(), project.getSlug(), project.getName(),
                ConverterJpaUtil.convertWokerToRecord(project.getProject()));
    }

}
