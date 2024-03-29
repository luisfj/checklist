package dev.luisjohann.checklist.infra.project.repository.memory;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import dev.luisjohann.checklist.domain.project.IWorkerRepository;
import dev.luisjohann.checklist.domain.project.Worker;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class WorkerRepositoryInMemory implements IWorkerRepository {

    private List<Worker> workers = new ArrayList<>();

    @Override
    public Mono<Worker> createWorker(Worker worker) {
        this.workers.add(worker);
        return Mono.just(worker);
    }

    @Override
    public Mono<Worker> findBySlugAndProjectSlug(String slug, String projectSlug) {
        var worker = this.workers.stream()
                .filter(w -> w.slug().equals(slug) && w.project().slug().equals(projectSlug)).findFirst()
                .orElse(null);
        return Objects.isNull(worker) ? Mono.empty() : Mono.just(worker);
    }

    @Override
    public Mono<Worker> findByNameAndProjectSlug(String name, String projectSlug) {
        var worker = this.workers.stream()
                .filter(w -> w.name().equals(name) && w.project().slug().equals(projectSlug)).findFirst()
                .orElse(null);
        return Objects.isNull(worker) ? Mono.empty() : Mono.just(worker);
    }

    @Override
    public Mono<Void> removeWorker(Worker worker) {
        this.workers.removeIf(w -> w.slug().equals(worker.slug())
                && w.project().slug().equals(worker.project().slug()));
        return Mono.empty();
    }

    @Override
    public Flux<Worker> findByProjectSlug(String slug) {
        return Flux.fromIterable(
                this.workers.stream().filter(w -> w.project().slug().equals(slug)).collect(Collectors.toList()));
    }

}
