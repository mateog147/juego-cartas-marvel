package com.sofkau.usecase.ronda.ganadorronda;

import com.sofkau.model.ronda.Ronda;
import com.sofkau.model.ronda.gateways.RondaRepository;
import com.sofkau.usecase.events.RondaTerminadaEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class GanadorRondaUseCase {
    private final RondaRepository repository;
    private final ApplicationEventPublisher publisher;
    public Mono<Ronda> terminarRonda (Ronda ronda, String nombreGanador) {

        if(ronda.getApuestas().size()<=0){
            ronda.next();
            ronda.setUltimoGanador(nombreGanador);
        }
        return repository.save(ronda).
                doOnSuccess(rondaGuardada -> this.publisher.publishEvent(new RondaTerminadaEvent(rondaGuardada)));
    }
}
