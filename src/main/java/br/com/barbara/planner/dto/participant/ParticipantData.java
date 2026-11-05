package br.com.barbara.planner.dto.participant;

import java.util.UUID;

public record ParticipantData(
        UUID id,
        String name,
        String email,
        Boolean isConfirmed
) {
}
