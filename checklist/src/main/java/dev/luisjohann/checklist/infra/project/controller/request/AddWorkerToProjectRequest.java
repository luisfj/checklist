package dev.luisjohann.checklist.infra.project.controller.request;

public record AddWorkerToProjectRequest(String workerName, String projectSlug) {
}
