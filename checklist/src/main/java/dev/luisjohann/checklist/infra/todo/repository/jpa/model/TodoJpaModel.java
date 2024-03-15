package dev.luisjohann.checklist.infra.todo.repository.jpa.model;

import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;

import dev.luisjohann.checklist.infra.project.repository.jpa.model.ProjectJpaModel;
import dev.luisjohann.checklist.infra.project.repository.jpa.model.WorkerJpaModel;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "todo")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TodoJpaModel {

        @Id
        @GeneratedValue(strategy = GenerationType.UUID)
        private UUID id;

        @Column(nullable = false)
        private String title;

        @Column(length = 2000)
        private String description;

        @ManyToOne(optional = false, fetch = FetchType.LAZY)
        @JoinColumn(name = "project_id", nullable = false)
        private ProjectJpaModel project;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "assigned_worker_id")
        private WorkerJpaModel assignedTo;

        @Temporal(TemporalType.TIMESTAMP)
        @CreatedDate
        @Column(name = "created_at")
        private LocalDateTime createdAt;

        @Temporal(TemporalType.TIMESTAMP)
        @UpdateTimestamp
        @Column(name = "updated_at")
        private LocalDateTime updatedAt;

        @Temporal(TemporalType.TIMESTAMP)
        @Column(name = "checked_at")
        private LocalDateTime checkedAt;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "checked_worker_id")
        private WorkerJpaModel checkedWorker;
}
