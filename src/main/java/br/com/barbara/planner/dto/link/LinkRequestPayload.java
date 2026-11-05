package br.com.barbara.planner.dto.link;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;

public record LinkRequestPayload(
        @NotBlank(message = "O título do link é obrigatório.")
        String title,

        @NotBlank(message = "A URL é obrigatória.")
        @URL(message = "A URL informada deve ser válida (exemplo: https://...).")
        String url
) {
}
