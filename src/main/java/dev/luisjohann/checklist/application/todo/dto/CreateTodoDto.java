package dev.luisjohann.checklist.application.todo.dto;

public record CreateTodoDto(String title, String description, String projectSlug, String workerAssignedSlug) {
}
