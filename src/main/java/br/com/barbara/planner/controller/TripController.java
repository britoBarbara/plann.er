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
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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
    public ResponseEntity<TripCreateResponse> createTrip(@RequestBody @Valid TripRequestPayload payload) {
        TripCreateResponse response = this.tripService.createTrip(payload);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Trip> getTripDetails(@PathVariable UUID id) {
        Trip trip = this.tripService.getTripDetails(id);
        return ResponseEntity.ok(trip);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Trip> updateTrip(@PathVariable UUID id, @RequestBody @Valid TripRequestPayload payload) {
        Trip updatedTrip = this.tripService.updateTrip(id, payload);
        return ResponseEntity.ok(updatedTrip);
    }

    @GetMapping("/{id}/confirm")
    public ResponseEntity<Trip> confirmTrip(@PathVariable UUID id) {
        Trip confirmedTrip = this.tripService.confirmTrip(id);
        return ResponseEntity.ok(confirmedTrip);
    }

    // --- Activity Endpoints ---

    @PostMapping("/{id}/activities")
    public ResponseEntity<ActivityResponse> registerActivity(
            @PathVariable UUID id,
            @RequestBody @Valid ActivityRequestPayload payload
    ) {
        Trip trip = this.tripService.findById(id);
        ActivityResponse activityResponse = this.activityService.registerActivity(payload, trip);
        return ResponseEntity.status(HttpStatus.CREATED).body(activityResponse);
    }

    @GetMapping("/{id}/activities")
    public ResponseEntity<List<ActivityData>> getAllActivities(@PathVariable UUID id) {
        this.tripService.findById(id);
        List<ActivityData> activityDataList = this.activityService.getAllActivitiesFromId(id);
        return ResponseEntity.ok(activityDataList);
    }

    // --- Participant Endpoints ---

    @GetMapping("/{id}/participants")
    public ResponseEntity<List<ParticipantData>> getAllParticipants(@PathVariable UUID id) {
        this.tripService.findById(id);
        List<ParticipantData> participantList = this.participantService.getAllParticipantsFromEvent(id);
        return ResponseEntity.ok(participantList);
    }

    @PostMapping("/{id}/invite")
    public ResponseEntity<ParticipantCreateResponse> inviteParticipant(
            @PathVariable UUID id,
            @RequestBody @Valid ParticipantRequestPayload payload
    ) {
        Trip trip = this.tripService.findById(id);
        ParticipantCreateResponse participantResponse = this.participantService.registerParticipantToEvent(payload.email(), trip);

        if (Boolean.TRUE.equals(trip.getIsConfirmed())) {
            this.participantService.triggerConfirmationEmailToParticipant(payload.email());
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(participantResponse);
    }

    // --- Link Endpoints ---

    @PostMapping("/{id}/links")
    public ResponseEntity<LinkResponse> registerLink(
            @PathVariable UUID id,
            @RequestBody @Valid LinkRequestPayload payload
    ) {
        Trip trip = this.tripService.findById(id);
        LinkResponse linkResponse = this.linkService.registerLink(payload, trip);
        return ResponseEntity.status(HttpStatus.CREATED).body(linkResponse);
    }

    @GetMapping("/{id}/links")
    public ResponseEntity<List<LinkData>> getAllLinks(@PathVariable UUID id) {
        this.tripService.findById(id);
        List<LinkData> linkDataList = this.linkService.getAllLinksFromTrip(id);
        return ResponseEntity.ok(linkDataList);
    }
}
