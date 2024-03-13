package dev.luisjohann.checklist.infra.todo.controller.response;

public record UpdateTodoResponse(String id, String title, String description, String workerAssignedSlug,
        String workerAssignedName) {

}
