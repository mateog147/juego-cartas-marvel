package com.sofkau.usecase.ronda.crearronda;

import com.sofkau.model.ronda.Ronda;
import com.sofkau.model.ronda.gateways.RondaRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class CrearRondaUseCase {
    private final RondaRepository repository;

    /** Creates a round and starts the betting timer (used when game is already in play). */
    public Mono<Ronda> crearRonda(Ronda ronda) {
        return crearRonda(ronda, true);
    }

    /** Idle round for lobby wait — no finRondaEn until the host starts the match. */
    public Mono<Ronda> crearRondaIdle(Ronda ronda) {
        return crearRonda(ronda, false);
    }

    private Mono<Ronda> crearRonda(Ronda ronda, boolean iniciarTimer) {
        if (ronda.getIsTimerOn() <= 0) {
            ronda.setIsTimerOn(30);
        }
        if (iniciarTimer) {
            ronda.iniciarTemporizador();
        } else {
            ronda.setFinRondaEn(null);
        }
        return repository.save(ronda);
    }
}
