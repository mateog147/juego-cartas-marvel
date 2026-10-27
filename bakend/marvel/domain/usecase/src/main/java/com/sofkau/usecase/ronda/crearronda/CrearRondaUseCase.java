package com.sofkau.usecase.ronda.crearronda;

import com.sofkau.model.ronda.Ronda;
import com.sofkau.model.ronda.gateways.RondaRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class CrearRondaUseCase {
    private final RondaRepository repository;

    public Mono<Ronda> crearRonda(Ronda ronda) {
        if (ronda.getIsTimerOn() <= 0) {
            ronda.setIsTimerOn(30);
        }
        ronda.iniciarTemporizador();
        return repository.save(ronda);
    }
}
