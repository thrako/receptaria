package dev.thrako.receptaria.service;

import dev.thrako.receptaria.model.repository.VisibilityRepository;
import org.springframework.stereotype.Service;

@Service
public class VisibilityService {

    private final VisibilityRepository visibilityRepository;

    public VisibilityService (VisibilityRepository visibilityRepository) {

        this.visibilityRepository = visibilityRepository;
    }
}
