package com.sofkau.usecase.carta.buscarcarta;

import com.sofkau.model.carta.Carta;
import com.sofkau.model.carta.gateways.CartaRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class BuscarCartaUseCase {

    private final CartaRepository repository;

    public Mono<Carta> buscarCarta(String id){
        return repository.findById(id);
    }

}
