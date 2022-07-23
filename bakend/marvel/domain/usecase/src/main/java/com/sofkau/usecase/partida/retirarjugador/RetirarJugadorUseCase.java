package com.sofkau.usecase.partida.retirarjugador;

import com.sofkau.model.partida.Partida;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class RetirarJugadorUseCase {

    public Mono<Partida> retirarJugador(Partida partida, String jugadorId){
        return Flux.fromIterable(partida.getJugadores())
                .map(jugador -> {
                    if(jugador.getId().equals(jugadorId)){
                        jugador.rendirse();
                    }
                    return jugador;
                })
                .collectList()
                .map(lista -> partida.toBuilder().jugadores(lista).build());

    }
}
