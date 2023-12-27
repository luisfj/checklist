package dev.luisjohann.checklist.infra.project.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import dev.luisjohann.checklist.application.project.RemoveWorkerFromProjectService;
import dev.luisjohann.checklist.application.project.dto.RemoveWorkerFromProjectDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/project")
@RequiredArgsConstructor
@Slf4j
public class RemoveWorkerFromProjectController {

        final RemoveWorkerFromProjectService service;

        @DeleteMapping("/worker/{project-slug}/{worker-name-slug}")
        @ResponseStatus(HttpStatus.OK)
        public Mono<ResponseEntity<Void>> removeWorker(
                        @PathVariable("project-slug") String projectSlug,
                        @PathVariable("worker-name-slug") String workerNameSlug) {
                log.info("REMOVE WORKER: {}, FROM PROJECT: {}!", workerNameSlug, projectSlug);

                return service.removeWorkerFromProject(
                                new RemoveWorkerFromProjectDto(workerNameSlug, projectSlug))
                                .flatMap(v -> Mono.just(ResponseEntity.ok().build()));
        }
}
