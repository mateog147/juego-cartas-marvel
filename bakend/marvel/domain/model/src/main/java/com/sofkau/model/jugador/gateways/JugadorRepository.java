package com.sofkau.model.jugador.gateways;

import com.sofkau.model.carta.Carta;
import com.sofkau.model.jugador.Jugador;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface JugadorRepository {
    Mono<Jugador> save(Jugador jugador);

    Flux<Jugador> findAll();

    Flux<Carta> agregarCartasGanadas(String jugadorId ,List<Carta> cartas);

    Mono<Jugador> rendirseEnRonda(String jugadorId, Jugador jugador);

    Mono<Jugador> eliminarcartaApostada(String jugadorId, Carta carta);


    Mono<Jugador> findById(String id);
}
