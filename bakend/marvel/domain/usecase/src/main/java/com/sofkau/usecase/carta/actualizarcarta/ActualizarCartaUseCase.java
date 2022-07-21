package com.sofkau.usecase.carta.actualizarcarta;

import com.sofkau.model.carta.Carta;
import com.sofkau.model.carta.gateways.CartaRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class ActualizarCartaUseCase {
    private final CartaRepository repository;

    public Mono<Carta> actualziarCarta(String id, Carta carta){
        return repository.update(id, carta);
    }
}
