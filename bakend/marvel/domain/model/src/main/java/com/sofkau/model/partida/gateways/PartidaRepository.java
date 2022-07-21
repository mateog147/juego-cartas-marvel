package com.sofkau.model.partida.gateways;

import com.sofkau.model.partida.Partida;
import reactor.core.publisher.Mono;

public interface PartidaRepository {
    Mono<Partida> save(Partida partida);
}
