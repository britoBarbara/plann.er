package br.com.barbara.planner.service;

import br.com.barbara.planner.domain.entity.Participant;
import br.com.barbara.planner.domain.entity.Trip;
import br.com.barbara.planner.dto.participant.ParticipantCreateResponse;
import br.com.barbara.planner.dto.participant.ParticipantData;
import br.com.barbara.planner.dto.participant.ParticipantRequestPayload;
import br.com.barbara.planner.exception.ParticipantNotFoundException;
import br.com.barbara.planner.repository.ParticipantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ParticipantService {

    private final ParticipantRepository repository;

    public void registerParticipantsToEvent(List<String> participantsToInvite, Trip trip) {
        if (participantsToInvite == null || participantsToInvite.isEmpty()) {
            return;
        }
        List<Participant> participants = participantsToInvite.stream()
                .map(email -> new Participant(email, trip))
                .toList();
        this.repository.saveAll(participants);
    }

    public ParticipantCreateResponse registerParticipantToEvent(String email, Trip trip) {
        Participant newParticipant = new Participant(email, trip);
        this.repository.save(newParticipant);
        return new ParticipantCreateResponse(newParticipant.getId());
    }

    public Participant confirmParticipant(UUID id, ParticipantRequestPayload payload) {
        Participant rawParticipant = this.repository.findById(id)
                .orElseThrow(() -> new ParticipantNotFoundException(id));
        rawParticipant.setIsConfirmed(true);
        rawParticipant.setName(payload.name());
        return this.repository.save(rawParticipant);
    }

    public void triggerConfirmationEmailToParticipants(UUID tripId) {
        // Envio de e-mails de confirmação (extensível para serviço de notificações)
    }

    public void triggerConfirmationEmailToParticipant(String email) {
        // Envio de e-mail de confirmação (extensível para serviço de notificações)
    }

    public List<ParticipantData> getAllParticipantsFromEvent(UUID tripId) {
        return this.repository.findByTripId(tripId).stream()
                .map(participant -> new ParticipantData(
                        participant.getId(),
                        participant.getName(),
                        participant.getEmail(),
                        participant.getIsConfirmed()
                ))
                .toList();
    }
}
