package dev.thrako.receptaria.controller.rest;

import dev.thrako.receptaria.repository.PhotoRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RestTestController {

    private final PhotoRepository photoRepository;

    public RestTestController (PhotoRepository photoRepository) {

        this.photoRepository = photoRepository;
    }

    @GetMapping("/api/test")
    public String test () {

        return photoRepository.getReferenceById(1L).getUrl();
    }

}
