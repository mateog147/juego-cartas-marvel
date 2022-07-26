package com.sofkau.model.jugador.gateways;

import com.sofkau.model.carta.Carta;
import com.sofkau.model.jugador.Jugador;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface JugadorRepository {
    Mono<Jugador> save(Jugador jugador);

    Flux<Jugador> findAll();

    Mono<Jugador> rendirseEnRonda(String jugadorId, Jugador jugador);

    Mono<Jugador> findById(String id);

    Flux<Jugador> findAllById(Iterable<String> ids);

    Mono<Jugador> findByUid(String uid);
}
