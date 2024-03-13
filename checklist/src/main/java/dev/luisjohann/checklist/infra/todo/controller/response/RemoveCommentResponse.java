package dev.luisjohann.checklist.infra.todo.controller.response;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

public record RemoveCommentResponse(String id, String comment, String todoId, String todoTitle, String todoDescription,
                String createdWorkerSlug, String createdWorkerName, String updatedWorkerSlug, String updatedWorkerName,
                String deleteWorkerSlug, String deleteWorkerName,
                @JsonFormat(pattern = "dd/MM/yyyy'T'HH:mm:ss", timezone = JsonFormat.DEFAULT_TIMEZONE) LocalDateTime createdAt,
                @JsonFormat(pattern = "dd/MM/yyyy'T'HH:mm:ss", timezone = JsonFormat.DEFAULT_TIMEZONE) LocalDateTime updatedAt,
                @JsonFormat(pattern = "dd/MM/yyyy'T'HH:mm:ss", timezone = JsonFormat.DEFAULT_TIMEZONE) LocalDateTime deletedAt) {

}
