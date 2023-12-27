package dev.luisjohann.checklist.infra.project.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import dev.luisjohann.checklist.application.project.FindBySlugProjectService;
import dev.luisjohann.checklist.infra.project.controller.response.FindProjectBySlugResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/project")
@RequiredArgsConstructor
@Slf4j
public class FindProjectBySlugController {

        final FindBySlugProjectService service;

        @GetMapping("/{slug}")
        @ResponseStatus(HttpStatus.OK)
        public Mono<ResponseEntity<FindProjectBySlugResponse>> getBySlug(@PathVariable("slug") String slug) {
                log.info("FIND PROJECT SLUG={}", slug);
                return service
                                .findBySlug(slug)
                                .flatMap(project -> Mono.just(
                                                ResponseEntity.ok().body(
                                                                new FindProjectBySlugResponse(project.getSlug(),
                                                                                project.getName(),
                                                                                project.getDescription()))));
        }
}
