package com.sofkau.usecase.partida.agregarronda;

import com.sofkau.model.partida.Partida;
import com.sofkau.model.ronda.Ronda;
import com.sofkau.usecase.ronda.crearronda.CrearRondaUseCase;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class AgregarRondaUseCase {
    private final CrearRondaUseCase crearRondaUseCase;

    /** Idle round (no timer) — used when creating a waiting table. */
    public Mono<Partida> agregarRonda(Partida partida) {
        return agregarRonda(partida, false);
    }

    public Mono<Partida> agregarRonda(Partida partida, boolean iniciarTimer) {
        Mono<Ronda> rondaMono = iniciarTimer
                ? crearRondaUseCase.crearRonda(new Ronda())
                : crearRondaUseCase.crearRondaIdle(new Ronda());
        return rondaMono.map(ronda -> partida.toBuilder().ronda(ronda).build());
    }
}
