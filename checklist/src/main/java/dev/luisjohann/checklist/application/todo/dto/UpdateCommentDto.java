package dev.luisjohann.checklist.application.todo.dto;

public record UpdateCommentDto(String id, String comment, String todoId, String workerSlug, String projectSlug) {
}
