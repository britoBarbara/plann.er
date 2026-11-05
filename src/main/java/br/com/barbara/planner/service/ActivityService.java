package br.com.barbara.planner.service;

import br.com.barbara.planner.domain.entity.Activity;
import br.com.barbara.planner.domain.entity.Trip;
import br.com.barbara.planner.dto.activity.ActivityData;
import br.com.barbara.planner.dto.activity.ActivityRequestPayload;
import br.com.barbara.planner.dto.activity.ActivityResponse;
import br.com.barbara.planner.exception.TripOperationException;
import br.com.barbara.planner.repository.ActivityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ActivityService {

    private final ActivityRepository repository;

    public ActivityResponse registerActivity(ActivityRequestPayload payload, Trip trip) {
        LocalDateTime occursAt = LocalDateTime.parse(payload.occursAt(), DateTimeFormatter.ISO_DATE_TIME);

        if (occursAt.isBefore(trip.getStartsAt()) || occursAt.isAfter(trip.getEndsAt())) {
            throw new TripOperationException(
                    "A data da atividade (" + occursAt + ") deve estar dentro do período da viagem ("
                            + trip.getStartsAt() + " até " + trip.getEndsAt() + ")."
            );
        }

        Activity newActivity = new Activity(payload.title(), payload.occursAt(), trip);
        this.repository.save(newActivity);
        return new ActivityResponse(newActivity.getId());
    }

    public List<ActivityData> getAllActivitiesFromId(UUID tripId) {
        return this.repository.findByTripId(tripId).stream()
                .map(activity -> new ActivityData(activity.getId(), activity.getTitle(), activity.getOccursAt()))
                .toList();
    }
}
