package org.example.backend.simulation;


import org.example.backend.user.CurrentUser;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/pets/{petId}/activity")
public class ActivityController {

    private final ActivityService activityService;
    private final CurrentUser currentUser;

    public ActivityController(ActivityService activityService, CurrentUser currentUser) {
        this.activityService = activityService;
        this.currentUser = currentUser;
    }

    @GetMapping
    public List<ActivityResponse> recent(@PathVariable Long petId,
                                         @RequestParam(defaultValue = "20") int limit,
                                         Principal principal) {
        return activityService.recent(petId, currentUser.from(principal), limit);
    }
}
