package dev.luisjohann.checklist.infra.todo.repository.jpa.model;

import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;

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
@Table(name = "comment")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentJpaModel {

        @Id
        @GeneratedValue(strategy = GenerationType.UUID)
        private UUID id;

        @ManyToOne(optional = false, fetch = FetchType.LAZY)
        @JoinColumn(name = "todo_id", nullable = false)
        private TodoJpaModel todo;

        @Column(length = 4000, nullable = false)
        private String comment;

        @ManyToOne(optional = false, fetch = FetchType.LAZY)
        @JoinColumn(name = "created_worker_id", nullable = false)
        private WorkerJpaModel createdWorker;

        @Temporal(TemporalType.TIMESTAMP)
        @CreatedDate
        @Column(name = "created_at")
        private LocalDateTime createdAt;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "updated_worker_id")
        private WorkerJpaModel updatedWorker;

        @Temporal(TemporalType.TIMESTAMP)
        @UpdateTimestamp
        @Column(name = "updated_at")
        private LocalDateTime updatedAt;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "deleted_worker_id")
        private WorkerJpaModel deletedWorker;

        @Temporal(TemporalType.TIMESTAMP)
        @Column(name = "deleted_at")
        private LocalDateTime deletedAt;

}
