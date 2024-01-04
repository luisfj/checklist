package dev.luisjohann.checklist.helper;

import org.apache.commons.lang3.RandomStringUtils;

import dev.luisjohann.checklist.domain.project.IProjectRepository;
import dev.luisjohann.checklist.domain.project.Project;

public class MockProjectHelper {

    public static final Project createBean(final IProjectRepository repository) {
        var bean = createNotPersistedBean();
        repository.createProject(bean);
        return bean;
    }

    public static final Project createNotPersistedBean() {
        return new Project(RandomStringUtils.randomAlphabetic(5).toLowerCase(),
                RandomStringUtils.randomAlphabetic(10).toUpperCase(), RandomStringUtils.randomAlphabetic(20));
    }

}
