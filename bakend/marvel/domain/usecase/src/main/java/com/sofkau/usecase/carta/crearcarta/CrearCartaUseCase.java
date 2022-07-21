package com.sofkau.usecase.carta.crearcarta;

import com.sofkau.model.carta.Carta;
import com.sofkau.model.carta.gateways.CartaRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class CrearCartaUseCase {

    private final CartaRepository repository;

    public Mono<Carta> saveCarta(Carta carta){
        return repository.save(carta);
    }
}
