package br.com.barbara.planner.exception;

import java.util.UUID;

public class ParticipantNotFoundException extends ResourceNotFoundException {
    public ParticipantNotFoundException(UUID id) {
        super("Participante não encontrado para o ID: " + id);
    }
}
