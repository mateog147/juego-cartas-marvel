package com.sofkau.model.carta.gateways;

import com.sofkau.model.carta.Carta;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CartaRepository {
    public Mono<Carta> save(Carta carta);

    public Mono<Carta> findById(Carta carta);

    public Mono<Carta> delete(Carta carta);

    public Flux<Carta> findAll();
}
