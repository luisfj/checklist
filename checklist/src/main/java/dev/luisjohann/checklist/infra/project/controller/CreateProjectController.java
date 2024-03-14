package dev.luisjohann.checklist.infra.project.controller;

import java.net.URI;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import dev.luisjohann.checklist.application.project.RegisterProjectService;
import dev.luisjohann.checklist.application.project.dto.RegisterProjectDto;
import dev.luisjohann.checklist.domain.project.Project;
import dev.luisjohann.checklist.infra.project.controller.request.CreateProjectRequest;
import dev.luisjohann.checklist.infra.project.controller.response.CreateProjectResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/project")
@RequiredArgsConstructor
@Slf4j
public class CreateProjectController {

        final RegisterProjectService service;

        @Operation(summary = "Create a project", tags = { "Project" })
        @PostMapping
        @ResponseStatus(HttpStatus.CREATED)
        public Mono<ResponseEntity<CreateProjectResponse>> createProject(@RequestBody CreateProjectRequest request) {
                log.info("CREATE NEW PROJECT!");
                return service.registerProject(new RegisterProjectDto(request.name(), request.description()))
                                .flatMap(this::convertFlatMap);
        }

        private Mono<ResponseEntity<CreateProjectResponse>> convertFlatMap(Project project) {
                return Mono.just(
                                ResponseEntity.created(URI.create(project.slug()))
                                                .body(new CreateProjectResponse(project.slug(),
                                                                project.name(),
                                                                project.description())));
        }
}
