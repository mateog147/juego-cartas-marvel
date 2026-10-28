package com.sofkau.api.realtime;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component("lobbyWebSocketHandler")
@RequiredArgsConstructor
public class LobbyWebSocketHandler implements WebSocketHandler {

    private final LobbyRealtimeHub hub;
    private final ObjectMapper objectMapper;

    @Override
    public Mono<Void> handle(WebSocketSession session) {
        Flux<org.springframework.web.reactive.socket.WebSocketMessage> outbound = hub.stream()
                .map(event -> {
                    try {
                        return objectMapper.writeValueAsString(event);
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                })
                .map(session::textMessage);

        Mono<Void> inbound = session.receive().then();

        return Mono.zip(session.send(outbound), inbound).then();
    }
}
