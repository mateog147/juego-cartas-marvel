package com.sofkau.api.realtime;

import com.sofkau.model.partida.Partida;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class PartidaRealtimeHub {

    private final Map<String, Sinks.Many<PartidaRealtimeEvent>> rooms = new ConcurrentHashMap<>();

    public Flux<PartidaRealtimeEvent> stream(String partidaId) {
        return room(partidaId).asFlux();
    }

    public void emit(String type, Partida partida) {
        if (partida == null || partida.getId() == null) {
            return;
        }
        PartidaRealtimeEvent event = PartidaRealtimeEvent.builder()
                .type(type)
                .partidaId(partida.getId())
                .partida(partida)
                .build();
        room(partida.getId()).tryEmitNext(event);
    }

    public void emitBetPlaced(Partida partida) {
        emit(PartidaRealtimeEvent.BET_PLACED, partida);
    }

    public void emitRoundResolved(Partida partida) {
        emit(PartidaRealtimeEvent.ROUND_RESOLVED, partida);
    }

    public void emitState(Partida partida) {
        emit(PartidaRealtimeEvent.STATE, partida);
    }

    public void emitPartidaCerrada(Partida partida) {
        emit(PartidaRealtimeEvent.PARTIDA_CERRADA, partida);
    }

    public void emitPartidaIniciada(Partida partida) {
        emit(PartidaRealtimeEvent.PARTIDA_INICIADA, partida);
    }

    private Sinks.Many<PartidaRealtimeEvent> room(String partidaId) {
        // replay last event so a client joining mid-round gets current snapshot
        return rooms.computeIfAbsent(partidaId,
                id -> Sinks.many().replay().limit(1));
    }
}
