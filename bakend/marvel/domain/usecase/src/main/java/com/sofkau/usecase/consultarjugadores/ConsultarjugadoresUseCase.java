package com.sofkau.usecase.consultarjugadores;

import com.sofkau.model.jugador.Jugador;
import com.sofkau.model.jugador.gateways.JugadorRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;

@RequiredArgsConstructor
public class ConsultarjugadoresUseCase {
    private final JugadorRepository repository;

    public Flux<Jugador> consultarJugadores(){
        return repository.findAll();
    }
}
