package br.com.barbara.planner.dto.ai;

import java.time.LocalDateTime;

public record ItineraryActivitySuggestionResponse(
        String title,
        String description,
        LocalDateTime occursAt
) {
}
