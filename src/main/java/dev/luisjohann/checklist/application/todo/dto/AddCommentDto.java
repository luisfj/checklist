package dev.luisjohann.checklist.application.todo.dto;

public record AddCommentDto(String comment, String todoId, String workerSlug, String projectSlug) {
}
