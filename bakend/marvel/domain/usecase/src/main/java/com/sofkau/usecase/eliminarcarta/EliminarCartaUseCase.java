package com.sofkau.usecase.eliminarcarta;

import com.sofkau.model.carta.Carta;
import com.sofkau.model.carta.gateways.CartaRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class EliminarCartaUseCase {

    private final CartaRepository repository;

    public Mono<Carta> eliminarCarta(Carta carta){
        return repository.delete(carta);
    }
}
