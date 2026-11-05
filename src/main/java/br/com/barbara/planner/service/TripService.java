package br.com.barbara.planner.service;

import br.com.barbara.planner.domain.entity.Trip;
import br.com.barbara.planner.dto.trip.TripCreateResponse;
import br.com.barbara.planner.dto.trip.TripRequestPayload;
import br.com.barbara.planner.repository.TripRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TripService {

    private final TripRepository tripRepository;
    private final ParticipantService participantService;

    public TripCreateResponse createTrip(TripRequestPayload payload) {
        Trip newTrip = new Trip(payload);
        this.tripRepository.save(newTrip);
        this.participantService.registerParticipantsToEvent(payload.emails_to_invite(), newTrip);
        return new TripCreateResponse(newTrip.getId());
    }

    public Optional<Trip> getTripDetails(UUID id) {
        return this.tripRepository.findById(id);
    }

    public Optional<Trip> updateTrip(UUID id, TripRequestPayload payload) {
        Optional<Trip> trip = this.tripRepository.findById(id);
        if (trip.isPresent()) {
            Trip rawTrip = trip.get();
            rawTrip.setEndsAt(LocalDateTime.parse(payload.ends_at(), DateTimeFormatter.ISO_DATE_TIME));
            rawTrip.setStartsAt(LocalDateTime.parse(payload.starts_at(), DateTimeFormatter.ISO_DATE_TIME));
            rawTrip.setDestination(payload.destination());
            this.tripRepository.save(rawTrip);
            return Optional.of(rawTrip);
        }
        return Optional.empty();
    }

    public Optional<Trip> confirmTrip(UUID id) {
        Optional<Trip> trip = this.tripRepository.findById(id);
        if (trip.isPresent()) {
            Trip rawTrip = trip.get();
            rawTrip.setIsConfirmed(true);
            this.participantService.triggerConfirmationEmailToParticipants(id);
            this.tripRepository.save(rawTrip);
            return Optional.of(rawTrip);
        }
        return Optional.empty();
    }

    public Optional<Trip> findById(UUID id) {
        return this.tripRepository.findById(id);
    }
}
