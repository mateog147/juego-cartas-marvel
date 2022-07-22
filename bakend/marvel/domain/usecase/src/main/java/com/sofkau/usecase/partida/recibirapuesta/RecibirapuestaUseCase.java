package com.sofkau.usecase.partida.recibirapuesta;

import com.sofkau.model.partida.Partida;
import com.sofkau.model.partida.gateways.PartidaRepository;
import com.sofkau.model.ronda.Ronda;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class RecibirapuestaUseCase {

    private final PartidaRepository repository;

    public Mono<Partida> recibirApuesta(Ronda ronda){

        return repository.save(new Partida()
                .toBuilder()
                .ronda(ronda)
                .build());
    }

}
