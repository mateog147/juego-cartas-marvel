package com.sofkau.usecase.partida.verificarganadorpartida;

import com.sofkau.model.jugador.Jugador;
import com.sofkau.model.partida.Partida;
import com.sofkau.usecase.jugador.actualizarpuntaje.ActualizarPuntajeJugadorUseCase;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.util.Collections;

@RequiredArgsConstructor
public class VerificarGanadorPartidaUseCase {
    private final ActualizarPuntajeJugadorUseCase actualizarPuntajeJugadorUseCase;

    public Mono<Partida> verificarGanadorPartida(Partida partida) {
        if (partida.getJugadores() != null && partida.getJugadores().size() == 1) {
            Jugador ganador = partida.getJugadores().get(0);
            return actualizarPuntajeJugadorUseCase
                    .actualizarPuntajeDelJugador(ganador.id(), ganador.puntaje() + 100)
                    .map(jugador -> {
                        jugador.setCartas(ganador.getCartas());
                        partida.setJugadores(Collections.singletonList(jugador));
                        partida.marcarCerrada(jugador.getId());
                        return partida;
                    });
        }
        return Mono.just(partida);
    }
}
