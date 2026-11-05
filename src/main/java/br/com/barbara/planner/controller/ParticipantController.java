package br.com.barbara.planner.controller;

import br.com.barbara.planner.domain.entity.Participant;
import br.com.barbara.planner.dto.participant.ParticipantRequestPayload;
import br.com.barbara.planner.service.ParticipantService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/participants")
@RequiredArgsConstructor
public class ParticipantController {

    private final ParticipantService participantService;

    @PostMapping("/{id}/confirm")
    public ResponseEntity<Participant> confirmParticipant(
            @PathVariable UUID id,
            @RequestBody @Valid ParticipantRequestPayload payload
    ) {
        Participant confirmedParticipant = this.participantService.confirmParticipant(id, payload);
        return ResponseEntity.ok(confirmedParticipant);
    }
}
