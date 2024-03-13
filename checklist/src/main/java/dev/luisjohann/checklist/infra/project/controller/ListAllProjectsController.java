package dev.luisjohann.checklist.infra.project.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import dev.luisjohann.checklist.application.project.ListAllProjectsService;
import dev.luisjohann.checklist.infra.project.controller.response.FindAllProjectsResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/project")
@RequiredArgsConstructor
@Slf4j
public class ListAllProjectsController {

        final ListAllProjectsService service;

        @GetMapping
        @ResponseStatus(HttpStatus.OK)
        public Flux<FindAllProjectsResponse> listAllProjects() {
                log.info("FIND ALL PROJECTS");
                return service
                                .listAllProjects()
                                .map(project -> new FindAllProjectsResponse(project.slug(), project.name(),
                                                project.description()))
                                .flatMap(Flux::just);
        }
}
