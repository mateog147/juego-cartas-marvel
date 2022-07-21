package com.sofkau.usecase.jugador.rendirse;

import com.sofkau.model.jugador.Jugador;
import com.sofkau.model.jugador.gateways.JugadorRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class RendirseEnRondaUseCase {
    private final JugadorRepository jugadorRepository;

    public Mono<Jugador> rendirseEnRonda(String id){

        return jugadorRepository.findById(id)
                .map(jugador -> jugador.toBuilder()
                        .cartas(jugador.rendirse())
                        .build())
                .flatMap(jugadorRepository::save);
    }
}
