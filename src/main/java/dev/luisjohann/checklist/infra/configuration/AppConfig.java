package dev.luisjohann.checklist.infra.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.config.EnableWebFlux;

import dev.luisjohann.checklist.domain.project.IProjectRepository;
import dev.luisjohann.checklist.domain.project.ISlugGenerator;
import dev.luisjohann.checklist.domain.project.IWorkerRepository;
import dev.luisjohann.checklist.infra.project.ProjectRepositoryInMemory;
import dev.luisjohann.checklist.infra.project.SlugGeneratorTest;
import dev.luisjohann.checklist.infra.project.WorkerRepositoryInMemory;

@Configuration
@EnableWebFlux
public class AppConfig {
    // implements WebFluxConfigurer {

    // @Override
    // public void addResourceHandlers(ResourceHandlerRegistry registry) {
    // registry.addResourceHandler("/resources/**")
    // .addResourceLocations("/public", "classpath:/static/")
    // .setCacheControl(CacheControl.maxAge(365, TimeUnit.DAYS));
    // }
    @Bean
    public IProjectRepository projectRepository() {
        return new ProjectRepositoryInMemory();
    }

    @Bean
    public ISlugGenerator slugGenerator() {
        return new SlugGeneratorTest();
    }

    @Bean
    public IWorkerRepository workerRepository() {
        return new WorkerRepositoryInMemory();
    }

}
