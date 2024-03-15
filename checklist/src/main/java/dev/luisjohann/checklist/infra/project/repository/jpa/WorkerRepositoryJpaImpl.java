package dev.luisjohann.checklist.infra.project.repository.jpa;

import java.util.Optional;
import java.util.function.Function;

import org.springframework.data.repository.NoRepositoryBean;

import dev.luisjohann.checklist.domain.project.IWorkerRepository;
import dev.luisjohann.checklist.domain.project.Worker;
import dev.luisjohann.checklist.infra.jpa.model.util.ConverterJpaUtil;
import dev.luisjohann.checklist.infra.project.repository.jpa.model.WorkerJpaModel;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@NoRepositoryBean
@RequiredArgsConstructor
public class WorkerRepositoryJpaImpl implements IWorkerRepository {

    final WorkerRepositoryJpa repositoryJpa;

    Function<Optional<WorkerJpaModel>, Mono<Worker>> fnWorkerOpt = (
            workerOpt) -> workerOpt.isPresent() ? Mono.just(ConverterJpaUtil.convertToRecord(workerOpt.get()))
                    : Mono.empty();

    @Override
    public Mono<Worker> createWorker(Worker worker) {
        var cWorker = ConverterJpaUtil.convertFromRecord(worker);
        var ret = ConverterJpaUtil.convertToRecord(this.repositoryJpa.save(cWorker));
        return Mono.just(ret);
    }

    @Override
    public Mono<Worker> findBySlugAndProjectSlug(String name, String projectSlug) {
        var workerOpt = this.repositoryJpa.findBySlugAndProjectSlug(name, projectSlug);
        return fnWorkerOpt.apply(workerOpt);
    }

    @Override
    public Mono<Worker> findByNameAndProjectSlug(String name, String projectSlug) {
        var workerOpt = this.repositoryJpa.findByNameAndProjectSlug(name, projectSlug);
        return fnWorkerOpt.apply(workerOpt);
    }

    @Override
    public Mono<Void> removeWorker(Worker worker) {
        repositoryJpa.delete(ConverterJpaUtil.convertFromRecord(worker));
        return Mono.empty();
    }

    @Override
    public Flux<Worker> findByProjectSlug(String slug) {
        return Flux.fromIterable(this.repositoryJpa.findByProjectSlug(slug).stream()
                .map(ConverterJpaUtil::convertToRecord).toList());
    }

}
