package br.com.barbara.planner.service;

import br.com.barbara.planner.domain.entity.Link;
import br.com.barbara.planner.domain.entity.Trip;
import br.com.barbara.planner.dto.link.LinkData;
import br.com.barbara.planner.dto.link.LinkRequestPayload;
import br.com.barbara.planner.dto.link.LinkResponse;
import br.com.barbara.planner.repository.LinkRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LinkService {

    private final LinkRepository repository;

    public LinkResponse registerLink(LinkRequestPayload payload, Trip trip) {
        Link newLink = new Link(payload.title(), payload.url(), trip);
        this.repository.save(newLink);
        return new LinkResponse(newLink.getId());
    }

    public List<LinkData> getAllLinksFromTrip(UUID tripId) {
        return this.repository.findByTripId(tripId).stream()
                .map(link -> new LinkData(link.getId(), link.getTitle(), link.getUrl()))
                .toList();
    }
}
