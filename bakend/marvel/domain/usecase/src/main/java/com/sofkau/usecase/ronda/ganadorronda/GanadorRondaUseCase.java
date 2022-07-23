package com.sofkau.usecase.ronda.ganadorronda;

import com.sofkau.model.ronda.Apuesta;
import com.sofkau.model.ronda.Ronda;
import com.sofkau.model.ronda.gateways.RondaRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class GanadorRondaUseCase {
    private final RondaRepository repository;

    public Mono<String> determinarGanadaorRonda (String rondaId) {

        return repository.findById(rondaId)
                .map(ronda -> ronda.determinarGanador());
    }
}
