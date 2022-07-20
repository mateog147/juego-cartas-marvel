package com.sofkau.usecase.mostrarcartas;

import com.sofkau.model.carta.Carta;
import com.sofkau.model.carta.gateways.CartaRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class MostrarCartasUseCase {

    private final CartaRepository repository;

    public Flux<Carta> mostrarCartas(){
        return repository.findAll();
    }
}
