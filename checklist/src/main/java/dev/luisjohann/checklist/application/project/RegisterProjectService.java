package dev.luisjohann.checklist.application.project;

import org.springframework.stereotype.Service;

import ch.qos.logback.core.testUtil.RandomUtil;
import dev.luisjohann.checklist.application.project.dto.RegisterProjectDto;
import dev.luisjohann.checklist.domain.project.IProjectRepository;
import dev.luisjohann.checklist.domain.project.ISlugGenerator;
import dev.luisjohann.checklist.domain.project.Project;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class RegisterProjectService {

    final IProjectRepository projectRepository;
    final ISlugGenerator slugGenerator;

    public Mono<Project> registerProject(RegisterProjectDto dto) {
        String slug = slugGenerator.generateSlugy(dto.name());

        while (projectRepository.findBySlug(slug).block() != null) {
            slug = slugGenerator.generateSlugy(slug += " " + RandomUtil.getPositiveInt());
        }

        Project project = buildProject(slug, dto);
        return projectRepository.createProject(project);
    }

    private Project buildProject(String slug, RegisterProjectDto dto) {
        return new Project(null, slug, dto.name(), dto.description());
    }
}
