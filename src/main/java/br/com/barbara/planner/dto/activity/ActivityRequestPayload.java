package br.com.barbara.planner.dto.activity;

public record ActivityRequestPayload(
        String title,
        String occurs_at
) {
}
