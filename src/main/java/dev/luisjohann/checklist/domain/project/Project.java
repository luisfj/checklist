package dev.luisjohann.checklist.domain.project;

import java.io.Serializable;

public class Project implements Serializable {

    private String slug;
    private String name;
    private String description;

    public Project(String slug, String name, String description) {
        this.slug = slug;
        this.name = name;
        this.description = description;
    }

    public String getSlug() {
        return slug;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

}
