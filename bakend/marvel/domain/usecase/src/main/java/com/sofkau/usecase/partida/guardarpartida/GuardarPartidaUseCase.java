package com.sofkau.usecase.partida.guardarpartida;

import com.sofkau.model.partida.Partida;
import com.sofkau.model.partida.gateways.PartidaRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class GuardarPartidaUseCase {
    private final PartidaRepository repository;
    public Mono<Partida> guardarPartida(Partida partida){
        return repository.save(partida);
    }
}
