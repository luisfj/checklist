package dev.luisjohann.checklist.infra.todo.repository.jpa;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dev.luisjohann.checklist.infra.todo.repository.jpa.model.CommentJpaModel;

@Repository
public interface CommentRepositoryJpa extends JpaRepository<CommentJpaModel, UUID> {

    @EntityGraph(attributePaths = { "todo", "todo.project", "createdWorker", "updatedWorker", "deletedWorker" })
    Optional<CommentJpaModel> findByIdAndTodoIdAndTodoProjectSlugAndDeletedAtIsNull(UUID id, UUID todoId,
            String projectSlug);

    @EntityGraph(attributePaths = { "todo", "todo.project", "createdWorker", "updatedWorker", "deletedWorker" })
    List<CommentJpaModel> findAllByTodoIdAndTodoProjectSlugAndDeletedAtIsNull(UUID todoId, String projectSlug);

}
