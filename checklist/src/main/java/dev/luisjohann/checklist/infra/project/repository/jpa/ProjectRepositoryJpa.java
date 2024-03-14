package dev.luisjohann.checklist.infra.project.repository.jpa;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dev.luisjohann.checklist.domain.project.Project;
import dev.luisjohann.checklist.infra.project.repository.jpa.model.ProjectJpaModel;

@Repository
public interface ProjectRepositoryJpa extends JpaRepository<ProjectJpaModel, Long> {

    Optional<Project> findBySlug(String slug);

    List<Project> findAllByOrderByNameAsc();

}
