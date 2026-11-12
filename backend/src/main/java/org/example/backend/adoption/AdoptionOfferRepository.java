package org.example.backend.adoption;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface AdoptionOfferRepository extends JpaRepository<AdoptionOffer, Long> {
    Optional<AdoptionOffer> findByUserIdAndStatus(Long userId, OfferStatus status);
    List<AdoptionOffer> findTop3ByUserIdOrderByCreatedAtDesc(Long userId);
    long countByUserIdAndStatusAndResolvedAtAfter(Long userId, OfferStatus status, Instant since);
}