package org.example.backend.simulation;

import org.example.backend.pet.PetStateService;
import org.example.backend.user.User;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ActivityService {

    private static final int MAX_LIMIT = 100;
    private final ActivityLogRepository logs;
    private final PetStateService state;

    public ActivityService(ActivityLogRepository logs, PetStateService state) {
        this.logs = logs;
        this.state = state;
    }

    @Transactional(readOnly = true)
    public List<ActivityResponse> recent(Long petId, User user, int limit) {
        state.findOwned(petId, user);                    // ownership check only
        Pageable page = PageRequest.of(0, Math.clamp(limit, 1, MAX_LIMIT));
        return logs.findByPetIdOrderByOccurredAtDesc(petId, page).stream()
                .map(ActivityResponse::from).toList();
    }
}
