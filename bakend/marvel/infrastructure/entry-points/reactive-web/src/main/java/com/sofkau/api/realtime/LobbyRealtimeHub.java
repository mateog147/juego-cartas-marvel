package com.sofkau.api.realtime;

import com.sofkau.model.jugador.Jugador;
import com.sofkau.model.partida.Partida;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class LobbyRealtimeHub {

    private final Sinks.Many<LobbyRealtimeEvent> sink =
            Sinks.many().multicast().onBackpressureBuffer();

    public Flux<LobbyRealtimeEvent> stream() {
        return sink.asFlux();
    }

    public void emitPerfilUpdated(Jugador jugador) {
        if (jugador == null || jugador.getId() == null) {
            return;
        }
        emit(LobbyRealtimeEvent.builder()
                .type(LobbyRealtimeEvent.PERFIL_UPDATED)
                .jugadorId(jugador.getId())
                .nombre(jugador.getNombre())
                .avatar(jugador.getAvatar())
                .build());
    }

    public void emitMesaCreada(Partida partida) {
        emitMesa(LobbyRealtimeEvent.MESA_CREADA, partida);
    }

    public void emitMesaCerrada(Partida partida) {
        emitMesa(LobbyRealtimeEvent.MESA_CERRADA, partida);
    }

    public void emitMesaIniciada(Partida partida) {
        emitMesa(LobbyRealtimeEvent.MESA_INICIADA, partida);
    }

    private void emitMesa(String type, Partida partida) {
        if (partida == null || partida.getId() == null) {
            return;
        }
        emit(LobbyRealtimeEvent.builder()
                .type(type)
                .partidaId(partida.getId())
                .hostJugadorId(partida.getHostJugadorId())
                .jugadorIds(rosterIds(partida))
                .partida(partida)
                .build());
    }

    private void emit(LobbyRealtimeEvent event) {
        sink.tryEmitNext(event);
    }

    private List<String> rosterIds(Partida partida) {
        if (partida.getJugadores() == null) {
            return Collections.emptyList();
        }
        return partida.getJugadores().stream()
                .map(Jugador::getId)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }
}
