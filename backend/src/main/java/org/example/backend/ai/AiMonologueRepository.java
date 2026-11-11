package org.example.backend.ai;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AiMonologueRepository extends JpaRepository<AiMonologue, Long> {
    List<AiMonologue> findTop5ByPetIdOrderBySaidAtDesc(Long petId);
}