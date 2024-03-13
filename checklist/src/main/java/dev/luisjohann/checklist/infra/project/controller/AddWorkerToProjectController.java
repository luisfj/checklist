package dev.luisjohann.checklist.infra.project.controller;

import java.net.URI;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import dev.luisjohann.checklist.application.project.AddWorkerToProjectService;
import dev.luisjohann.checklist.application.project.dto.AddWorkerToProjectDto;
import dev.luisjohann.checklist.domain.project.Worker;
import dev.luisjohann.checklist.infra.project.controller.request.AddWorkerToProjectRequest;
import dev.luisjohann.checklist.infra.project.controller.response.AddWorkerToProjectResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/worker")
@RequiredArgsConstructor
@Slf4j
public class AddWorkerToProjectController {

        final AddWorkerToProjectService service;

        @PostMapping
        @ResponseStatus(HttpStatus.CREATED)
        public Mono<ResponseEntity<AddWorkerToProjectResponse>> addWorker(
                        @RequestBody AddWorkerToProjectRequest request) {
                log.info("ADD WORKER TO PROJECT!");

                return service.addWorkerToProject(
                                new AddWorkerToProjectDto(request.workerName(), request.projectSlug()))
                                .flatMap(this::convertFlatMap);
        }

        private Mono<ResponseEntity<AddWorkerToProjectResponse>> convertFlatMap(Worker worker) {
                return Mono.just(
                                ResponseEntity.created(URI.create(worker.slug()))
                                                .body(new AddWorkerToProjectResponse(worker.slug(),
                                                                worker.name())));
        }
}
