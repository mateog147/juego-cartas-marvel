package com.sofkau.model.carta.gateways;

import com.sofkau.model.carta.Carta;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CartaRepository {
    public Mono<Carta> save(Carta carta);

    public Mono<Carta> findById(String id);

    public Mono<Void> deleteById(String id);

    public Flux<Carta> findAll();

    public Mono<Carta> update(String id, Carta carta);
}
