package com.sofkau.usecase.partida.verificarganadorpartida;

import com.sofkau.model.jugador.Jugador;
import com.sofkau.model.partida.Partida;
import com.sofkau.usecase.jugador.actualizarpuntaje.ActualizarPuntajeJugadorUseCase;
import com.sofkau.usecase.events.RondaTerminadaEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import reactor.core.publisher.Mono;

import java.util.Arrays;

@RequiredArgsConstructor
public class VerificarGanadorPartidaUseCase {
    private final ActualizarPuntajeJugadorUseCase actualizarPuntajeJugadorUseCase;


    public Mono<Partida> verificarGanadorPartida(Partida partida){
        if(partida.getJugadores().size() == 1){
            Jugador ganador = partida.getJugadores().get(0);
            return actualizarPuntajeJugadorUseCase.actualizarPuntajeDelJugador(ganador.id(), ganador.puntaje() + 100)
                    .map(jugador -> {
                        jugador.setCartas(ganador.getCartas());
                        return Arrays.asList(jugador);
                    })
                    .map(lista -> partida.toBuilder().jugadores(lista).build());
        }
        return Mono.just(partida);
    }
}

