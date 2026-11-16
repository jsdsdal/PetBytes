package org.example.backend.adoption;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface AdoptionOfferRepository extends JpaRepository<AdoptionOffer, Long> {

    List<AdoptionOffer> findByUserIdAndStatus(Long userId, OfferStatus status);

    List<AdoptionOffer> findByUserIdAndStatusOrderByCreatedAtAsc(Long userId, OfferStatus status);

    List<AdoptionOffer> findTop4ByUserIdOrderByCreatedAtDesc(Long userId);

    @Query("""
        SELECT COUNT(DISTINCT o.batchId)
        FROM AdoptionOffer o
        WHERE o.user.id = :userId
          AND o.status = org.example.backend.adoption.OfferStatus.DECLINED
          AND o.resolvedAt > :since
        """)
    
    long countDistinctDeclinedBatchesSince(@Param("userId") Long userId, @Param("since") Instant since);
}