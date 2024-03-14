package dev.luisjohann.checklist.infra.project.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import dev.luisjohann.checklist.application.project.ListWorkersFromProjectBySlugService;
import dev.luisjohann.checklist.infra.project.controller.response.WorkersFromProjectResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/worker")
@RequiredArgsConstructor
@Slf4j
public class ListWorkersFromProjectBySlugController {

        final ListWorkersFromProjectBySlugService service;

        @Operation(summary = "Retrieve all project workers", tags = { "Project" })
        @GetMapping("/{project-slug}")
        @ResponseStatus(HttpStatus.OK)
        public Flux<WorkersFromProjectResponse> findWorkersByProjectSlug(
                        @PathVariable("project-slug") String projectSlug) {
                log.info("FIND WORKERS FROM PROJECT SLUG={}", projectSlug);
                return service
                                .findWorkersByProjectSlug(projectSlug)
                                .map(worker -> new WorkersFromProjectResponse(worker.slug(), worker.name()))
                                .flatMap(Flux::just);
        }
}
