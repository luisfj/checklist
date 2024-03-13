package dev.luisjohann.checklist.application.todo.dto;

public record UpdateTodoDto(String id, String title, String description, String projectSlug,
        String workerAssignedSlug) {
}
