package com.sofkau.usecase.jugador.agregarcartas;

import com.sofkau.model.carta.Carta;
import com.sofkau.model.carta.gateways.CartaRepository;
import com.sofkau.model.jugador.Jugador;
import com.sofkau.model.jugador.gateways.JugadorRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class AgregarCartasUseCase {
    private final JugadorRepository repository;
    private final CartaRepository cartaRepository;

    public Mono<Jugador> agregarCartas(String jugadorId, List<Carta> cartasNuevas){
        return repository.findById(jugadorId)
                .map(jugador -> jugador.toBuilder()
                        .cartas(jugador.agregarCartas(cartasNuevas))
                        .build()
                )
                .flatMap(this.repository::save);
    }
}
