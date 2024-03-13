package dev.luisjohann.checklist.application.todo.dto;

public record RemoveCommentDto(String id, String todoId, String workerSlug, String projectSlug) {
}
