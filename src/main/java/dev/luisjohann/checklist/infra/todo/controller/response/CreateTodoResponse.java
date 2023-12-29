package dev.luisjohann.checklist.infra.todo.controller.response;

public record CreateTodoResponse(String id, String title, String description, String workerAssignedSlug,
                String workerAssignedName) {

}
