package com.sofkau.model.ronda.gateways;

import com.sofkau.model.carta.Carta;
import com.sofkau.model.jugador.Jugador;
import com.sofkau.model.ronda.Ronda;
import reactor.core.publisher.Mono;

public interface RondaRepository {

    Mono<Ronda> save(Ronda ronda);
    Mono<Ronda> findById();
    //Mono<Ronda> agregarApuesta(String id, );

}
