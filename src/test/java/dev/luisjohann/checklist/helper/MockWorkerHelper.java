package dev.luisjohann.checklist.helper;

import dev.luisjohann.checklist.domain.project.IWorkerRepository;
import dev.luisjohann.checklist.domain.project.Project;
import dev.luisjohann.checklist.domain.project.Worker;

public class MockWorkerHelper {

    private static final String WORKER_SLUG = "worker_test";
    private static final String WORKER_NAME = "Worker Name Test";
    private static final String WORKER_SLUG_OHER = "other_worker_test";
    private static final String WORKER_NAME_OHER = "Other Worker Name Test";
    private static final String WORKER_SLUG_INVALID = "worker_test_not_exists";

    public static final Worker createBean(final IWorkerRepository repository, final Project project) {
        var bean = new Worker(WORKER_SLUG, WORKER_NAME, project);
        repository.createWorker(bean);
        return bean;
    }

    public static final Worker createOtherBean(final IWorkerRepository repository, final Project project) {
        var bean = new Worker(WORKER_SLUG_OHER, WORKER_NAME_OHER, project);
        repository.createWorker(bean);
        return bean;
    }

    public static final Worker createNotPersistBean(final Project project) {
        return new Worker(WORKER_SLUG_INVALID, WORKER_SLUG_INVALID, project);
    }

}
