package com.sofkau.usecase.jugador.eliminarcartaapostada;

import com.sofkau.model.carta.Carta;
import com.sofkau.model.carta.gateways.CartaRepository;
import com.sofkau.model.jugador.Jugador;
import com.sofkau.model.jugador.gateways.JugadorRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class EliminarCartaApostadaUseCase {
    private final JugadorRepository jugadorRepository;

    public Mono<Jugador> eliminarCartaApostada(String id, String cartaId){

        return jugadorRepository.findById(id)
                        .map(jugador -> jugador.toBuilder()
                                .cartas(jugador.eliminarCarta(cartaId))
                                .build())
                .flatMap(this.jugadorRepository::save);
    }
}
