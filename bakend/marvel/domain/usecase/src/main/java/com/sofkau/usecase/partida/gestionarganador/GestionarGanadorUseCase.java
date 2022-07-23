package com.sofkau.usecase.partida.gestionarganador;

import com.sofkau.model.partida.Partida;
import com.sofkau.usecase.ronda.ganadorronda.GanadorRondaUseCase;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class GestionarGanadorUseCase {
    private GanadorRondaUseCase ganadorRondaUseCase;
    public Mono<Partida> gestionarGanador(Partida partida){
        String ganadorId = partida.getRonda().determinarGanador();
        return  Flux.fromIterable(partida.getJugadores())
                                    .map(jugador -> {
                                        if(jugador.id().equals(ganadorId)){
                                            jugador.agregarCartas(partida.getRonda().entregarCartas());
                                        }
                                        return jugador;
                                    }).collectList()
                                    .map(lista -> partida.toBuilder().jugadores(lista).build());

    }
}
