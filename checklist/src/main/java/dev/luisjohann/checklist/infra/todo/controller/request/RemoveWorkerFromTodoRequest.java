package dev.luisjohann.checklist.infra.todo.controller.request;

public record RemoveWorkerFromTodoRequest(String todoId, String projectSlug) {

}
