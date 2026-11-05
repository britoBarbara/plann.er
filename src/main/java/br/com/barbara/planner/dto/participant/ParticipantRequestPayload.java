package br.com.barbara.planner.dto.participant;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ParticipantRequestPayload(
        @NotBlank(message = "O nome do participante é obrigatório.")
        @Size(min = 2, max = 255, message = "O nome deve ter entre 2 e 255 caracteres.")
        String name,

        @NotBlank(message = "O e-mail do participante é obrigatório.")
        @Email(message = "O e-mail informado é inválido.")
        String email
) {
}
