package com.sofkau.model.ronda.gateways;


import com.sofkau.model.ronda.Ronda;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface RondaRepository {

    Mono<Ronda> save(Ronda ronda);
    Mono<Ronda> findById(String id);
    Flux<Ronda> findAll();

}
