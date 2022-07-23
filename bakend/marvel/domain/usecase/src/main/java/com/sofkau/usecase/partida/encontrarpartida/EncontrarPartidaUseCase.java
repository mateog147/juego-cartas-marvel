package com.sofkau.usecase.partida.encontrarpartida;

import com.sofkau.model.partida.Partida;
import com.sofkau.model.partida.gateways.PartidaRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class EncontrarPartidaUseCase {
    private final PartidaRepository repository;
    public Mono<Partida> encontrarPartida(String id){
        return repository.findById(id);
    }
}
