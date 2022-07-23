package com.sofkau.usecase.partida.agregarronda;

import com.sofkau.model.partida.Partida;
import com.sofkau.model.ronda.Ronda;
import com.sofkau.usecase.ronda.crearronda.CrearRondaUseCase;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class AgregarRondaUseCase {
    private final CrearRondaUseCase crearRondaUseCase;
    public Mono<Partida> agregarRonda(Partida partida){
        return crearRondaUseCase.crearRonda(new Ronda())
                .map(ronda -> partida.toBuilder().ronda(ronda).build());
    }
}
