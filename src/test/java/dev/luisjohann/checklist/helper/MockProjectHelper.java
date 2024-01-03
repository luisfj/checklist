package dev.luisjohann.checklist.helper;

import dev.luisjohann.checklist.domain.project.IProjectRepository;
import dev.luisjohann.checklist.domain.project.Project;

public class MockProjectHelper {

    private static final String PROJECT_SLUG = "test_project";
    private static final String PROJECT_OTHER_SLUG = "test_project_other";
    private static final String PROJECT_INVALID_SLUG = "test_project_not_exists";

    public static final Project createBean(final IProjectRepository repository) {
        var bean = new Project(PROJECT_SLUG, PROJECT_SLUG, null);
        repository.createProject(bean);
        return bean;
    }

    public static final Project createOtherBean(final IProjectRepository repository) {
        var bean = new Project(PROJECT_OTHER_SLUG, PROJECT_OTHER_SLUG, null);
        repository.createProject(bean);
        return bean;
    }

    public static final Project createNotPersistBean() {
        return new Project(PROJECT_INVALID_SLUG, PROJECT_INVALID_SLUG, null);
    }

}
