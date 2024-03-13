package dev.luisjohann.checklist.infra.todo.controller.response;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

public record AddWorkerToTodoResponse(String id, String title, String description, String workerAssignedSlug,
                String workerAssignedName,
                @JsonFormat(pattern = "dd/MM/yyyy'T'HH:mm:ss", timezone = JsonFormat.DEFAULT_TIMEZONE) LocalDateTime createdAt,
                @JsonFormat(pattern = "dd/MM/yyyy'T'HH:mm:ss", timezone = JsonFormat.DEFAULT_TIMEZONE) LocalDateTime updatedAt,
                @JsonFormat(pattern = "dd/MM/yyyy'T'HH:mm:ss", timezone = JsonFormat.DEFAULT_TIMEZONE) LocalDateTime checkedAt,
                String workerCheckedSlug, String workerCheckedName) {

}
