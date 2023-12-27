package dev.luisjohann.checklist.infra.project;

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
                .filter(w -> w.getSlug().equals(slug) && w.getProject().getSlug().equals(projectSlug)).findFirst()
                .orElse(null);
        return Objects.isNull(worker) ? Mono.empty() : Mono.just(worker);
    }

    @Override
    public Mono<Worker> findByNameAndProjectSlug(String name, String projectSlug) {
        var worker = this.workers.stream()
                .filter(w -> w.getName().equals(name) && w.getProject().getSlug().equals(projectSlug)).findFirst()
                .orElse(null);
        return Objects.isNull(worker) ? Mono.empty() : Mono.just(worker);
    }

    @Override
    public Mono<Void> removeWorker(Worker worker) {
        this.workers.removeIf(w -> w.getSlug().equals(worker.getSlug())
                && w.getProject().getSlug().equals(worker.getProject().getSlug()));
        return Mono.empty();
    }

    @Override
    public Flux<Worker> findByProjectSlug(String slug) {
        return Flux.fromIterable(
                this.workers.stream().filter(w -> w.getProject().getSlug().equals(slug)).collect(Collectors.toList()));
    }

}
