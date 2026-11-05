package br.com.barbara.planner.controller;

import br.com.barbara.planner.domain.entity.Trip;
import br.com.barbara.planner.dto.activity.ActivityData;
import br.com.barbara.planner.dto.activity.ActivityRequestPayload;
import br.com.barbara.planner.dto.activity.ActivityResponse;
import br.com.barbara.planner.dto.link.LinkData;
import br.com.barbara.planner.dto.link.LinkRequestPayload;
import br.com.barbara.planner.dto.link.LinkResponse;
import br.com.barbara.planner.dto.participant.ParticipantCreateResponse;
import br.com.barbara.planner.dto.participant.ParticipantData;
import br.com.barbara.planner.dto.participant.ParticipantRequestPayload;
import br.com.barbara.planner.dto.trip.TripCreateResponse;
import br.com.barbara.planner.dto.trip.TripRequestPayload;
import br.com.barbara.planner.service.ActivityService;
import br.com.barbara.planner.service.LinkService;
import br.com.barbara.planner.service.ParticipantService;
import br.com.barbara.planner.service.TripService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/trips")
@RequiredArgsConstructor
public class TripController {

    private final TripService tripService;
    private final ParticipantService participantService;
    private final ActivityService activityService;
    private final LinkService linkService;

    // --- Trip Endpoints ---

    @PostMapping
    public ResponseEntity<TripCreateResponse> createTrip(@RequestBody TripRequestPayload payload) {
        TripCreateResponse response = this.tripService.createTrip(payload);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Trip> getTripDetails(@PathVariable UUID id) {
        return this.tripService.getTripDetails(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Trip> updateTrip(@PathVariable UUID id, @RequestBody TripRequestPayload payload) {
        return this.tripService.updateTrip(id, payload)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/confirm")
    public ResponseEntity<Trip> confirmTrip(@PathVariable UUID id) {
        return this.tripService.confirmTrip(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // --- Activity Endpoints ---

    @PostMapping("/{id}/activities")
    public ResponseEntity<ActivityResponse> registerActivity(@PathVariable UUID id, @RequestBody ActivityRequestPayload payload) {
        Optional<Trip> trip = this.tripService.findById(id);

        if (trip.isPresent()) {
            ActivityResponse activityResponse = this.activityService.registerActivity(payload, trip.get());
            return ResponseEntity.ok(activityResponse);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{id}/activities")
    public ResponseEntity<List<ActivityData>> getAllActivities(@PathVariable UUID id) {
        List<ActivityData> activityDataList = this.activityService.getAllActivitiesFromId(id);
        return ResponseEntity.ok(activityDataList);
    }

    // --- Participant Endpoints ---

    @GetMapping("/{id}/participants")
    public ResponseEntity<List<ParticipantData>> getAllParticipants(@PathVariable UUID id) {
        List<ParticipantData> participantList = this.participantService.getAllParticipantsFromEvent(id);
        return ResponseEntity.ok(participantList);
    }

    @PostMapping("/{id}/invite")
    public ResponseEntity<ParticipantCreateResponse> inviteParticipant(@PathVariable UUID id, @RequestBody ParticipantRequestPayload payload) {
        Optional<Trip> trip = this.tripService.findById(id);

        if (trip.isPresent()) {
            Trip rawTrip = trip.get();
            ParticipantCreateResponse participantResponse = this.participantService.registerParticipantToEvent(payload.email(), rawTrip);

            if (Boolean.TRUE.equals(rawTrip.getIsConfirmed())) {
                this.participantService.triggerConfirmationEmailToParticipant(payload.email());
            }

            return ResponseEntity.ok(participantResponse);
        }
        return ResponseEntity.notFound().build();
    }

    // --- Link Endpoints ---

    @PostMapping("/{id}/links")
    public ResponseEntity<LinkResponse> registerLink(@PathVariable UUID id, @RequestBody LinkRequestPayload payload) {
        Optional<Trip> trip = this.tripService.findById(id);

        if (trip.isPresent()) {
            LinkResponse linkResponse = this.linkService.registerLink(payload, trip.get());
            return ResponseEntity.ok(linkResponse);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{id}/links")
    public ResponseEntity<List<LinkData>> getAllLinks(@PathVariable UUID id) {
        List<LinkData> linkDataList = this.linkService.getAllLinksFromTrip(id);
        return ResponseEntity.ok(linkDataList);
    }
}
