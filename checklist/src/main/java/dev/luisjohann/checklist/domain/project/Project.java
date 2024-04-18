package dev.luisjohann.checklist.domain.project;

import java.io.Serializable;

import dev.luisjohann.checklist.domain.project.exceptions.InvalidProjectDescriptionException;
import dev.luisjohann.checklist.domain.project.exceptions.InvalidProjectNameException;
import io.micrometer.common.util.StringUtils;

public record Project(Long id, String slug, String name, String description) implements Serializable {

    public Project {
        if (StringUtils.isBlank(slug) || StringUtils.isBlank(name)) {
            throw new InvalidProjectNameException();
        }
        if (StringUtils.isNotBlank(description) && description.length() > 50) {
            throw new InvalidProjectDescriptionException();
        }
    }
}
