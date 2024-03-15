package dev.luisjohann.checklist.infra.project.repository.jpa;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dev.luisjohann.checklist.infra.project.repository.jpa.model.WorkerJpaModel;

@Repository
public interface WorkerRepositoryJpa extends JpaRepository<WorkerJpaModel, Long> {

    @EntityGraph(attributePaths = { "project" })
    Optional<WorkerJpaModel> findBySlugAndProjectSlug(String slug, String projectSlug);

    @EntityGraph(attributePaths = { "project" })
    Optional<WorkerJpaModel> findByNameAndProjectSlug(String name, String projectSlug);

    @EntityGraph(attributePaths = { "project" })
    List<WorkerJpaModel> findByProjectSlug(String projectSlug);

}
