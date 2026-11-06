package br.com.barbara.planner.dto.ai;

import java.util.List;

public record SmartItineraryResponse(
        String summary,
        List<ItineraryActivitySuggestionResponse> suggestions
) {
}
