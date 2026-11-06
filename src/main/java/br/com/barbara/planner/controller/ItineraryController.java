package br.com.barbara.planner.controller;

import br.com.barbara.planner.dto.ai.SmartItineraryResponse;
import br.com.barbara.planner.service.ItineraryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.UUID;

@RestController
@RequestMapping("/trips")
public class ItineraryController {

    private final ItineraryService itineraryService;

    public ItineraryController(ItineraryService itineraryService) {
        this.itineraryService = itineraryService;
    }

    @PostMapping("/{tripId}/generate-itinerary")
    public ResponseEntity<SmartItineraryResponse> generateItinerary(@PathVariable UUID tripId) {
        SmartItineraryResponse itinerary = this.itineraryService.generateSmartItinerary(tripId);
        return ResponseEntity.ok(itinerary);
    }

}
