package dev.thrako.receptaria.service.schedule;

import dev.thrako.receptaria.model.entity.photo.TempPhotoEntity;
import dev.thrako.receptaria.service.TempPhotoService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.function.Predicate;

import static dev.thrako.receptaria.common.constant.Constants.MAX_ALLOWED_TEMP_PHOTO_DURATION;

@Component
public class TempPhotoCleaner {

    private final TempPhotoService tempPhotoService;

    public TempPhotoCleaner (TempPhotoService tempPhotoService) {

        this.tempPhotoService = tempPhotoService;
    }

    //6h = 6 * 60min * 60sec * 1000nano
    @Scheduled(fixedRate = 6 * 60 * 60 * 1000L)
    private void checkExpired () {



        final Predicate<TempPhotoEntity> expired = e -> {

            final Duration durationPast = Duration.between(e.getAddedOn(), LocalDateTime.now());
            return MAX_ALLOWED_TEMP_PHOTO_DURATION.compareTo(durationPast) < 0;
        };

        tempPhotoService.getAll()
                .stream()
                .filter(expired)
                .forEach(this.tempPhotoService::delete);
    }
}
