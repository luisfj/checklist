package dev.luisjohann.checklist.infra.todo.controller.request;

public record CreateTodoRequest(String title, String description, String workerAssignedSlug) {

}
