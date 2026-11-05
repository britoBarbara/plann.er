package br.com.barbara.planner.dto.activity;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ActivityRequestPayload(
        @NotBlank(message = "O título da atividade é obrigatório.")
        @Size(min = 3, max = 255, message = "O título deve ter entre 3 e 255 caracteres.")
        String title,

        @NotBlank(message = "A data e hora da atividade são obrigatórias (formato ISO-8601).")
        @JsonProperty("occursAt")
        @JsonAlias({"occurs_at", "occursAt"})
        String occursAt
) {
}
