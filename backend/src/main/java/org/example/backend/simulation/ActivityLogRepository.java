package org.example.backend.simulation;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ActivityLogRepository extends JpaRepository<ActivityLog, Long> {
    Optional<ActivityLog> findByPetId(Long petId);
    Optional<ActivityLog> findByPetIdAndType(Long petId, ActivityType activity);

}
