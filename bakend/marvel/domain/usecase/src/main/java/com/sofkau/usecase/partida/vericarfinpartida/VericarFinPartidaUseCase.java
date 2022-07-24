package com.sofkau.usecase.partida.vericarfinpartida;

import com.sofkau.model.jugador.Jugador;
import com.sofkau.model.partida.Partida;
import com.sofkau.usecase.jugador.actualizarpuntaje.ActualizarPuntajeJugadorUseCase;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class VericarFinPartidaUseCase {

    private ActualizarPuntajeJugadorUseCase actualizarUseCase;
    public Mono<Partida> verificarGanador(Partida partida){
        return Mono.just(partida);
        /*Flux.fromIterable(partida.getJugadores())
                .filter(jugador -> jugador.cartas().size()>0)
                .collectList()
                .map(list -> {
                    if (list.size()==1){
                        Jugador ganador = list.get(0);
                        return actualizarUseCase.actualizarPuntajeDelJugador(ganador.id(), ganador.getPuntaje()+100);
                    }
                }).then();*/
    }
}
