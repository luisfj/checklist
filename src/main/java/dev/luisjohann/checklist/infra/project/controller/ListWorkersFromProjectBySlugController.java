package dev.luisjohann.checklist.infra.project.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import dev.luisjohann.checklist.application.project.ListWorkersFromProjectBySlugService;
import dev.luisjohann.checklist.infra.project.controller.response.WorkersFromProjectResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/project")
@RequiredArgsConstructor
@Slf4j
public class ListWorkersFromProjectBySlugController {

        final ListWorkersFromProjectBySlugService service;

        @GetMapping("/worker/{project-slug}")
        @ResponseStatus(HttpStatus.OK)
        public Flux<WorkersFromProjectResponse> findWorkersByProjectSlug(
                        @PathVariable("project-slug") String projectSlug) {
                log.info("FIND WORKERS FROM PROJECT SLUG={}", projectSlug);
                return service
                                .findWorkersByProjectSlug(projectSlug)
                                .map(worker -> new WorkersFromProjectResponse(worker.getSlug(), worker.getName()))
                                .flatMap(Flux::just);
        }
}
