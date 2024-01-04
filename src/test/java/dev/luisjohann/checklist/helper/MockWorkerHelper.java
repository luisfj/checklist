package dev.luisjohann.checklist.helper;

import org.apache.commons.lang3.RandomStringUtils;

import dev.luisjohann.checklist.domain.project.IWorkerRepository;
import dev.luisjohann.checklist.domain.project.Project;
import dev.luisjohann.checklist.domain.project.Worker;

public class MockWorkerHelper {

    public static final Worker createBean(final IWorkerRepository repository, final Project project) {
        var bean = createNotPersistBean(project);
        repository.createWorker(bean);
        return bean;
    }

    public static final Worker createNotPersistBean(final Project project) {
        return new Worker(RandomStringUtils.randomAlphabetic(7).toLowerCase(),
                RandomStringUtils.randomAlphabetic(15).toUpperCase(), project);
    }

}
