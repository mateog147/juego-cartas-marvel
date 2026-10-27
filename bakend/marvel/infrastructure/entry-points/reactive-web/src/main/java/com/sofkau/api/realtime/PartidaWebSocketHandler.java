package com.sofkau.api.realtime;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component("partidaWebSocketHandler")
@RequiredArgsConstructor
public class PartidaWebSocketHandler implements WebSocketHandler {

    private final PartidaRealtimeHub hub;
    private final ObjectMapper objectMapper;

    @Override
    public Mono<Void> handle(WebSocketSession session) {
        String partidaId = extractPartidaId(session);
        if (partidaId == null || partidaId.isBlank()) {
            return session.close();
        }

        Flux<org.springframework.web.reactive.socket.WebSocketMessage> outbound = hub.stream(partidaId)
                .map(event -> {
                    try {
                        return objectMapper.writeValueAsString(event);
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                })
                .map(session::textMessage);

        // Ignore inbound client messages for now (commands stay on HTTP)
        Mono<Void> inbound = session.receive().then();

        return Mono.zip(session.send(outbound), inbound).then();
    }

    private String extractPartidaId(WebSocketSession session) {
        String path = session.getHandshakeInfo().getUri().getPath();
        if (path == null) {
            return null;
        }
        // Expected: /ws/partida/{partidaId}
        String marker = "/ws/partida/";
        int idx = path.indexOf(marker);
        if (idx < 0) {
            return null;
        }
        String id = path.substring(idx + marker.length());
        int slash = id.indexOf('/');
        return slash >= 0 ? id.substring(0, slash) : id;
    }
}
