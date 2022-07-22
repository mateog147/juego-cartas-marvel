package com.sofkau.usecase.partida.repartircartas;


import com.sofkau.model.jugador.gateways.JugadorRepository;
import com.sofkau.model.partida.Partida;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.util.*;

@RequiredArgsConstructor
public class RepartirCartasUseCase {

    private final JugadorRepository jugadorRepository;
    public Mono<Partida> repartir(Partida partida, List<String> jugadoresIds) {
        return jugadorRepository.findAllById(jugadoresIds)
                .map(jugador -> {
                    partida.getMazo().barajar();
                    jugador.setCartas( partida.getMazo().asignarCartas());
                    return jugador;
                })
                .collectList()
                .map(list -> partida.toBuilder().jugadores(list).build());

    }
}
