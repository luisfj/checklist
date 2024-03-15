package dev.luisjohann.checklist.infra.project.repository.jpa.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "project")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectJpaModel {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;
        @Column(nullable = false)
        private String slug;
        @Column(nullable = false)
        private String name;
        @Column(length = 2000)
        private String description;
}
