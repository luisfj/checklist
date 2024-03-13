package dev.luisjohann.checklist.domain.project;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IWorkerRepository {

    Mono<Worker> createWorker(Worker worker);

    Mono<Worker> findBySlugAndProjectSlug(String name, String projectSlug);

    Mono<Worker> findByNameAndProjectSlug(String name, String projectSlug);

    Mono<Void> removeWorker(Worker worker);

    Flux<Worker> findByProjectSlug(String slug);
}
