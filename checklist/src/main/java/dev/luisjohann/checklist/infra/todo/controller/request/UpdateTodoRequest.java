package dev.luisjohann.checklist.infra.todo.controller.request;

public record UpdateTodoRequest(String title, String description, String workerAssignedSlug) {

}
