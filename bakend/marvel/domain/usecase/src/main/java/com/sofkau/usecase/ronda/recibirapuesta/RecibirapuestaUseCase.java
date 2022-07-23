package com.sofkau.usecase.ronda.recibirapuesta;

import com.sofkau.model.carta.Carta;
import com.sofkau.model.ronda.Apuesta;
import com.sofkau.model.ronda.Ronda;
import com.sofkau.model.ronda.gateways.RondaRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
