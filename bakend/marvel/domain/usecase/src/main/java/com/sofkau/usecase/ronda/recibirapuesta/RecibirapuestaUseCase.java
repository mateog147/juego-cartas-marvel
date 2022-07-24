package com.sofkau.usecase.ronda.recibirapuesta;

import com.sofkau.model.ronda.values.Apuesta;
import com.sofkau.model.ronda.Ronda;
import com.sofkau.model.ronda.gateways.RondaRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class RecibirapuestaUseCase {
    private final RondaRepository repository;

        public Mono<Ronda> recibirApuesta (String rondaId, Apuesta apuesta) {

            return repository.findById(rondaId)
                    .map(ronda -> {
                        ronda.agregarApuesta(apuesta);
                        return ronda;
                    })
                    .flatMap(ronda -> repository.save(ronda));
        }




}
