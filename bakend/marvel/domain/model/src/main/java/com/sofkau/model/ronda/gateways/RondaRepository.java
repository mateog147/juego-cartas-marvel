package com.sofkau.model.ronda.gateways;


import com.sofkau.model.carta.Carta;
import com.sofkau.model.ronda.Ronda;
import reactor.core.publisher.Mono;

import java.util.Map;

public interface RondaRepository {

    Mono<Ronda> save(Ronda ronda);
    Mono<Ronda> findById(String id);
    //Mono<Ronda> agregarApuesta(String rondaId, Map<Carta, String> apuesta);

}
