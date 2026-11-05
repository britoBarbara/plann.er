package br.com.barbara.planner.dto.trip;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.List;

public record TripRequestPayload(
        @NotBlank(message = "O destino é obrigatório e não pode ser vazio.")
        @Size(min = 3, max = 255, message = "O destino deve ter entre 3 e 255 caracteres.")
        String destination,

        @NotBlank(message = "A data e hora de início são obrigatórias (formato ISO-8601).")
        @JsonProperty("startsAt")
        @JsonAlias({"starts_at", "startsAt"})
        String startsAt,

        @NotBlank(message = "A data e hora de término são obrigatórias (formato ISO-8601).")
        @JsonProperty("endsAt")
        @JsonAlias({"ends_at", "endsAt"})
        String endsAt,

        @JsonProperty("emailsToInvite")
        @JsonAlias({"emails_to_invite", "emailsToInvite"})
        List<@Email(message = "E-mail de convidado inválido.") String> emailsToInvite,

        @NotBlank(message = "O e-mail do organizador é obrigatório.")
        @Email(message = "O e-mail do organizador informado é inválido.")
        @JsonProperty("ownerEmail")
        @JsonAlias({"owner_email", "ownerEmail"})
        String ownerEmail,

        @NotBlank(message = "O nome do organizador é obrigatório.")
        @Size(min = 2, max = 255, message = "O nome do organizador deve ter entre 2 e 255 caracteres.")
        @JsonProperty("ownerName")
        @JsonAlias({"owner_name", "ownerName"})
        String ownerName
) {
}
