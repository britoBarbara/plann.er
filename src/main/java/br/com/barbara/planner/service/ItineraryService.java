package br.com.barbara.planner.service;

import br.com.barbara.planner.domain.entity.Trip;
import br.com.barbara.planner.dto.ai.SmartItineraryResponse;
import br.com.barbara.planner.exception.ResourceNotFoundException;
import br.com.barbara.planner.repository.TripRepository;
import dev.langchain4j.model.chat.ChatLanguageModel;
import java.util.UUID;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;

@Service
public class ItineraryService {
    private final TripRepository tripRepository;
    private final ChatLanguageModel chatLanguageModel;

    public ItineraryService(TripRepository tripRepository, ChatLanguageModel chatLanguageModel) {
        this.tripRepository = tripRepository;
        this.chatLanguageModel = chatLanguageModel;
    }

    public SmartItineraryResponse generateSmartItinerary(UUID tripId) {
        Trip trip = this.tripRepository.findById(tripId)
                .orElseThrow(() -> new ResourceNotFoundException("Viagem não encontrada com o ID: " + tripId));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        String startsAtFormatted = trip.getStartsAt().format(formatter);
        String endsAtFormatted = trip.getEndsAt().format(formatter);

        String prompt = String.format(
                "Você é um guia turístico especialista. Crie um roteiro personalizado para uma viagem a %s " +
                        "com início em %s e término em %s. " +
                        "Forneça um resumo geral do roteiro e sugestões de atividades com título, descrição e horário estimado dentro da janela da viagem.",
                trip.getDestination(), startsAtFormatted, endsAtFormatted
        );

        String aiResponse = this.chatLanguageModel.generate(prompt);

        return new SmartItineraryResponse(aiResponse, java.util.Collections.emptyList());
    }
}
