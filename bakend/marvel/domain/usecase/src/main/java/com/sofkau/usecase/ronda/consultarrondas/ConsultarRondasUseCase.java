package com.sofkau.usecase.ronda.consultarrondas;

import com.sofkau.model.jugador.Jugador;
import com.sofkau.model.ronda.Ronda;
import com.sofkau.model.ronda.gateways.RondaRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;

@RequiredArgsConstructor
public class ConsultarRondasUseCase {

    private final RondaRepository repository;

    public Flux<Ronda> consultarRondas(){
        return repository.findAll();
    }
}
