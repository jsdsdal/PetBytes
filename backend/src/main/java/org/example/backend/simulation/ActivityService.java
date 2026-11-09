package org.example.backend.simulation;

import org.springframework.stereotype.Service;

@Service
public class ActivityService {

    private final ActivityLogRepository repository;

    public ActivityService(ActivityLogRepository repository) { this.repository = repository; }


}
