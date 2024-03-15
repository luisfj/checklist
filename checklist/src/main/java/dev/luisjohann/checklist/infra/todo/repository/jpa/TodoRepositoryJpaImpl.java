package dev.luisjohann.checklist.infra.todo.repository.jpa;

import java.util.Objects;
import java.util.UUID;
import java.util.function.Function;
import java.util.function.Supplier;

import org.springframework.data.repository.NoRepositoryBean;

import dev.luisjohann.checklist.domain.todo.ITodoRepository;
import dev.luisjohann.checklist.domain.todo.Todo;
import dev.luisjohann.checklist.domain.todo.exception.TodoNotFoundException;
import dev.luisjohann.checklist.infra.jpa.model.util.ConverterJpaUtil;
import dev.luisjohann.checklist.infra.todo.repository.jpa.model.TodoJpaModel;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@NoRepositoryBean
@RequiredArgsConstructor
public class TodoRepositoryJpaImpl implements ITodoRepository {

    final TodoRepositoryJpa repositoryJpa;

    @Override
    public Mono<Todo> createTodo(Todo todo) {
        var cTodo = ConverterJpaUtil.convertRecordToTodo(todo);
        var ret = ConverterJpaUtil.convertTodoToRecord(repositoryJpa.save(cTodo));
        return Mono.just(ret);
    }

    @Override
    public Mono<Todo> updateTodo(Todo todo) {
        return verifyTodoExists(todo,
                (bdTodo) -> {
                    bdTodo.setTitle(todo.title());
                    bdTodo.setDescription(todo.description());
                    bdTodo.setAssignedTo(ConverterJpaUtil.convertRecordToWorker(todo.assignedTo()));
                    bdTodo.setCheckedAt(todo.checkedAt());
                    bdTodo.setCheckedWorker(ConverterJpaUtil.convertRecordToWorker(todo.checkedWorker()));

                    this.repositoryJpa.save(bdTodo);
                    return Mono.just(todo);
                },
                () -> Mono.error(new TodoNotFoundException(todo.id().toString(), todo.project().slug())));
    }

    @Override
    public Mono<Void> removeTodo(Todo todo) {
        return verifyTodoExists(todo,
                (bdTodo) -> {
                    this.repositoryJpa.delete(bdTodo);
                    return Mono.empty();
                },
                () -> Mono.error(new TodoNotFoundException(todo.id().toString(), todo.project().slug())));
    }

    @Override
    public Mono<Todo> findByIdAndProjectSlug(UUID id, String projectSlug) {
        return verifyTodoExists(id, projectSlug,
                (todoModel) -> Mono.just(ConverterJpaUtil.convertTodoToRecord(todoModel)), Mono::empty);
    }

    @Override
    public Flux<Todo> listAllTodosByProjectSlug(String projectSlug) {
        return Flux.fromIterable(
                this.repositoryJpa.findAllByProjectSlug(projectSlug).stream().map(ConverterJpaUtil::convertTodoToRecord)
                        .toList());
    }

    private <T> Mono<T> verifyTodoExists(Todo todo, Function<TodoJpaModel, Mono<T>> exists,
            Supplier<Mono<T>> notExists) {
        if (Objects.isNull(todo)) {
            throw new TodoNotFoundException(null, null);
        }

        return verifyTodoExists(todo.id(), todo.project().slug(), exists, notExists);
    }

    private <T> Mono<T> verifyTodoExists(UUID id, String projectSlug, Function<TodoJpaModel, Mono<T>> exists,
            Supplier<Mono<T>> notExists) {
        var todo = this.repositoryJpa.findByIdAndProjectSlug(id, projectSlug);
        if (!todo.isPresent()) {
            return notExists.get();
        }
        return exists.apply(todo.get());
    }
}
