package dev.luisjohann.checklist.domain.project;

import java.io.Serializable;
import java.util.Objects;

import dev.luisjohann.checklist.domain.project.exceptions.InvalidWorkerNameException;
import dev.luisjohann.checklist.domain.project.exceptions.ProjectRequiredException;
import io.micrometer.common.util.StringUtils;

public class Worker implements Serializable {

    private String slug;
    private String name;
    private Project project;

    public Worker(String slug, String name, Project project) {
        if (StringUtils.isBlank(slug) || StringUtils.isBlank(name)) {
            throw new InvalidWorkerNameException();
        }
        if (Objects.isNull(project)) {
            throw new ProjectRequiredException();
        }
        this.slug = slug;
        this.name = name;
        this.project = project;
    }

    public String getName() {
        return name;
    }

    public Project getProject() {
        return project;
    }

    public String getSlug() {
        return slug;
    }
}
