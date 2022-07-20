package com.sofkau.usecase.jugador.rendirse;

import com.sofkau.model.jugador.Jugador;
import com.sofkau.model.jugador.gateways.JugadorRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class RendirseEnRondaUseCase {
    private final JugadorRepository jugadorRepository;

    Mono<Jugador> rendirse(String id, Jugador jugador){
        return jugadorRepository.rendirseEnRonda(id, jugador);
    }
}
