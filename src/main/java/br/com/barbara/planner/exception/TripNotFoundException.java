package br.com.barbara.planner.exception;

import java.util.UUID;

public class TripNotFoundException extends ResourceNotFoundException {
    public TripNotFoundException(UUID id) {
        super("Viagem não encontrada para o ID: " + id);
    }
}
