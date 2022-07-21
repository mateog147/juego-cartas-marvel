package com.sofkau.usecase.jugador.actualizarpuntaje;

import com.sofkau.model.jugador.Jugador;
import com.sofkau.model.jugador.gateways.JugadorRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class ActualizarPuntajeJugadorUseCase {
    private final JugadorRepository jugadorRepository;

    public Mono<Jugador> actualizarPuntajeDelJugador(String id, Integer puntos){
        return jugadorRepository
                .findById(id)
                .switchIfEmpty(Mono.error(new Exception("Id jugador invalido")))
                .map(jugador -> jugador.toBuilder()
                        .puntaje(puntos)
                        .build()
                )
                .flatMap(jugador -> this.jugadorRepository.save(jugador));
    }
}
