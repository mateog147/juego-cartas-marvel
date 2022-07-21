package com.sofkau.usecase.ronda.agregarapuesta;

import com.sofkau.model.carta.Carta;
import com.sofkau.model.ronda.Ronda;
import com.sofkau.model.ronda.gateways.RondaRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class AgregarApuestaUseCase {

    private RondaRepository repository;

    public Mono<Ronda> agregarApuesta(Carta carta, String jugadorId){
        return null;
    }
}
