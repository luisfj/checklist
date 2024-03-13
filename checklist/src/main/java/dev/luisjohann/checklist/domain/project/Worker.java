package dev.luisjohann.checklist.domain.project;

import java.io.Serializable;
import java.util.Objects;

import dev.luisjohann.checklist.domain.project.exceptions.InvalidWorkerNameException;
import dev.luisjohann.checklist.domain.project.exceptions.ProjectRequiredException;
import io.micrometer.common.util.StringUtils;

public record Worker(String slug, String name, Project project) implements Serializable {

    public Worker {
        if (StringUtils.isBlank(slug) || StringUtils.isBlank(name)) {
            throw new InvalidWorkerNameException();
        }
        if (Objects.isNull(project)) {
            throw new ProjectRequiredException();
        }
    }
}
