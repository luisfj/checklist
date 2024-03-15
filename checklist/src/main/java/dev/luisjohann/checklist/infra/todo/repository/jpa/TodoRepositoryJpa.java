package dev.luisjohann.checklist.infra.todo.repository.jpa;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dev.luisjohann.checklist.infra.todo.repository.jpa.model.TodoJpaModel;

@Repository
public interface TodoRepositoryJpa extends JpaRepository<TodoJpaModel, UUID> {

    @EntityGraph(attributePaths = { "project", "assignedTo", "checkedWorker" })
    Optional<TodoJpaModel> findByIdAndProjectSlug(UUID id, String projectSlug);

    @EntityGraph(attributePaths = { "project", "assignedTo", "checkedWorker" })
    List<TodoJpaModel> findAllByProjectSlug(String projectSlug);

}
