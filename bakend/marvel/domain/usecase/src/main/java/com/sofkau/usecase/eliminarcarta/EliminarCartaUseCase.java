package com.sofkau.usecase.eliminarcarta;

import com.sofkau.model.carta.Carta;
import com.sofkau.model.carta.gateways.CartaRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class EliminarCartaUseCase {

    private final CartaRepository repository;

    public Mono<Void> eliminarCarta(String id){
        return repository.deleteById(id);
    }
}
