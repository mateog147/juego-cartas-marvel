package com.sofkau.usecase.partida.gestionarganador;

import com.sofkau.model.partida.Partida;
import com.sofkau.usecase.ronda.ganadorronda.GanadorRondaUseCase;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class GestionarGanadorUseCase {
    private final GanadorRondaUseCase ganadorRondaUseCase;
    private String nombreGanador;
    public Mono<Partida> gestionarGanador(Partida partida){
        String ganadorId = partida.getRonda().determinarGanador();
        System.out.println("GANO:" + ganadorId);
        return  Flux.fromIterable(partida.getJugadores())
                                    .map(jugador -> {
                                        if(jugador.id().equals(ganadorId)){
                                            nombreGanador = jugador.getNombre();
                                            jugador.agregarCartas(partida.getRonda().entregarCartas());
                                        }
                                        return jugador;
                                    })
                                    .filter(jugador -> !jugador.cartas().isEmpty())
                                     .log()
                                    .collectList()
                                    .map(lista -> {
                                        partida.setJugadores(lista);
                                        return partida;
                                    })
                                    .flatMap(partida1 -> {
                                        return ganadorRondaUseCase.terminarRonda(partida1.getRonda(), nombreGanador);
                                    })
                                    .map(ronda -> partida.toBuilder().ronda(ronda).build());

    }
}
