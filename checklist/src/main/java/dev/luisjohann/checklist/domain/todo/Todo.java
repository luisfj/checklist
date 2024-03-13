package dev.luisjohann.checklist.domain.todo;

import java.io.Serializable;
import java.time.LocalDateTime;

import dev.luisjohann.checklist.domain.project.Project;
import dev.luisjohann.checklist.domain.project.Worker;

public class Todo implements Serializable {

    private String id;
    private String title;
    private String description;
    private Project project;
    private Worker assignedTo;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime checkedAt;
    private Worker checkedWorker;

    public Todo(String id, String title, String description, Project project, Worker assignedTo,
            LocalDateTime createdAt, LocalDateTime updatedAt, LocalDateTime checkedAt, Worker checkedWorker) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.project = project;
        this.assignedTo = assignedTo;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.checkedAt = checkedAt;
        this.checkedWorker = checkedWorker;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Project getProject() {
        return project;
    }

    public Worker getAssignedTo() {
        return assignedTo;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public LocalDateTime getCheckedAt() {
        return checkedAt;
    }

    public Worker getCheckedWorker() {
        return checkedWorker;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Todo other = (Todo) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

}
