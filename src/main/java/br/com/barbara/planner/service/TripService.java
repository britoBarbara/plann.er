package br.com.barbara.planner.service;

import br.com.barbara.planner.domain.entity.Trip;
import br.com.barbara.planner.dto.trip.TripCreateResponse;
import br.com.barbara.planner.dto.trip.TripRequestPayload;
import br.com.barbara.planner.exception.InvalidTripDatesException;
import br.com.barbara.planner.exception.TripNotFoundException;
import br.com.barbara.planner.repository.TripRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TripService {

    private final TripRepository tripRepository;
    private final ParticipantService participantService;

    public TripCreateResponse createTrip(TripRequestPayload payload) {
        LocalDateTime startsAt = LocalDateTime.parse(payload.startsAt(), DateTimeFormatter.ISO_DATE_TIME);
        LocalDateTime endsAt = LocalDateTime.parse(payload.endsAt(), DateTimeFormatter.ISO_DATE_TIME);

        if (endsAt.isBefore(startsAt)) {
            throw new InvalidTripDatesException("A data de término da viagem (" + endsAt + ") não pode ser anterior à data de início (" + startsAt + ").");
        }

        Trip newTrip = new Trip(payload);
        this.tripRepository.save(newTrip);

        this.participantService.registerParticipantsToEvent(payload.emailsToInvite(), newTrip);
        return new TripCreateResponse(newTrip.getId());
    }

    public Trip getTripDetails(UUID id) {
        return this.tripRepository.findById(id)
                .orElseThrow(() -> new TripNotFoundException(id));
    }

    public Trip updateTrip(UUID id, TripRequestPayload payload) {
        Trip rawTrip = this.getTripDetails(id);

        LocalDateTime startsAt = LocalDateTime.parse(payload.startsAt(), DateTimeFormatter.ISO_DATE_TIME);
        LocalDateTime endsAt = LocalDateTime.parse(payload.endsAt(), DateTimeFormatter.ISO_DATE_TIME);

        if (endsAt.isBefore(startsAt)) {
            throw new InvalidTripDatesException("A data de término da viagem (" + endsAt + ") não pode ser anterior à data de início (" + startsAt + ").");
        }

        rawTrip.setEndsAt(endsAt);
        rawTrip.setStartsAt(startsAt);
        rawTrip.setDestination(payload.destination());
        return this.tripRepository.save(rawTrip);
    }

    public Trip confirmTrip(UUID id) {
        Trip rawTrip = this.getTripDetails(id);
        rawTrip.setIsConfirmed(true);
        this.participantService.triggerConfirmationEmailToParticipants(id);
        return this.tripRepository.save(rawTrip);
    }

    public Trip findById(UUID id) {
        return this.getTripDetails(id);
    }
}
