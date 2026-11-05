package br.com.barbara.planner.dto.link;

import java.util.UUID;

public record LinkData(
        UUID id,
        String title,
        String url
) {
}
