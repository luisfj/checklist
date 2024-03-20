package dev.luisjohann.checklist.application.project;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import dev.luisjohann.checklist.application.project.dto.RegisterProjectDto;
import dev.luisjohann.checklist.domain.project.IProjectRepository;
import dev.luisjohann.checklist.domain.project.ISlugGenerator;
import dev.luisjohann.checklist.domain.project.Project;
import reactor.core.publisher.Mono;

public class RegisterProjectServiceTest {

    IProjectRepository projectRepository;
    ISlugGenerator slugGenerator;
    RegisterProjectService registerProjectService;
    Project project;

    @BeforeEach
    void setUp() {
        projectRepository = mock(IProjectRepository.class);
        slugGenerator = mock(ISlugGenerator.class);
        registerProjectService = new RegisterProjectService(projectRepository, slugGenerator);
        project = mock(Project.class);
    }

    @Test
    void testRegisterProject() {
        when(slugGenerator.generateSlugy(any(String.class))).thenReturn("slug");
        when(projectRepository.findBySlug(any(String.class))).thenReturn(Mono.empty());
        when(projectRepository.createProject(any(Project.class))).thenReturn(Mono.just(project));

        var registeredProject = registerProjectService.registerProject(new RegisterProjectDto("Name", "Description"));

        verify(slugGenerator, times(1)).generateSlugy(any(String.class));
        verify(projectRepository, times(1)).findBySlug(any(String.class));
        verify(projectRepository, times(1)).createProject(any(Project.class));
        assertEquals(project, registeredProject.block());

    }

    @Test
    void testRegisterProjectTwoSlugGenCall() {
        when(slugGenerator.generateSlugy(anyString())).thenReturn("slug");
        when(projectRepository.findBySlug(anyString())).thenReturn(Mono.just(project), Mono.empty());
        when(projectRepository.createProject(any(Project.class))).thenReturn(Mono.just(project));

        var registeredProject = registerProjectService.registerProject(new RegisterProjectDto("Name", "Description"));

        verify(slugGenerator, times(2)).generateSlugy(anyString());
        verify(projectRepository, times(2)).findBySlug(anyString());
        verify(projectRepository, times(1)).createProject(any(Project.class));
        assertEquals(project, registeredProject.block());

    }
}
