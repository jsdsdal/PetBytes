package org.example.backend.simulation;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ActivityLogRepository extends JpaRepository<ActivityLog, Long> {
    List<ActivityLog> findByPetIdOrderByOccurredAtDesc(Long petId, Pageable pageable);
    List<ActivityLog> findByPetIdAndTypeNotOrderByOccurredAtDesc(Long petId, ActivityType type);
    List<ActivityLog> findTop5ByPetIdAndTypeOrderByOccurredAtDesc(Long petId, ActivityType type);
    long countByPetIdAndTypeNot(Long petId, ActivityType type);
}
