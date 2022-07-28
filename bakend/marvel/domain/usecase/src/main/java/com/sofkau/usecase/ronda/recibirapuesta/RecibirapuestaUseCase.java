package com.sofkau.usecase.ronda.recibirapuesta;

import com.sofkau.model.ronda.values.Apuesta;
import com.sofkau.model.ronda.Ronda;
import com.sofkau.model.ronda.gateways.RondaRepository;
import com.sofkau.usecase.events.ApuestaAgregaEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class RecibirapuestaUseCase {
    private final ApplicationEventPublisher publisher;
    private final RondaRepository repository;

        public Mono<Ronda> recibirApuesta (String rondaId, Apuesta apuesta) {

            return repository.findById(rondaId)
                    .map(ronda -> {
                        ronda.agregarApuesta(apuesta);
                        return ronda;
                    })
                    .flatMap(ronda -> repository.save(ronda))
                    .doOnSuccess(ronda -> this.publisher.publishEvent(new ApuestaAgregaEvent(ronda)));
        }




}
