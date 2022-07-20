package com.sofkau.usecase.jugador.crearjugador;

import com.sofkau.model.jugador.Jugador;
import com.sofkau.model.jugador.gateways.JugadorRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class CrearJugadorUseCase {
    private final JugadorRepository jugadorRepository;

    public Mono<Jugador> crearNuevoJugador(Jugador jugador){
        return jugadorRepository.save(jugador);
    }
}
