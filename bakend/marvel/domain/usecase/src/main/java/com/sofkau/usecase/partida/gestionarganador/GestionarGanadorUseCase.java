package com.sofkau.usecase.partida.gestionarganador;

import com.sofkau.model.carta.Carta;
import com.sofkau.model.jugador.Jugador;
import com.sofkau.model.jugador.gateways.JugadorRepository;
import com.sofkau.model.partida.Partida;
import com.sofkau.usecase.ronda.ganadorronda.GanadorRondaUseCase;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class GestionarGanadorUseCase {
    private final GanadorRondaUseCase ganadorRondaUseCase;

    private final JugadorRepository repository;
    private String nombreGanador;
    public Mono<Partida> gestionarGanador(Partida partida){

        String ganadorId = partida.getRonda().determinarGanador();
        nombreGanador = repository.findById(ganadorId).toString();

        return Flux.fromIterable(partida.getJugadores())
                                    .map(jugador -> {
                                        if(jugador.id().equals(ganadorId)){
                                            nombreGanador = jugador.getNombre();
                                            jugador.agregarCartas(partida.getRonda().entregarCartas());
                                        }
                                        return jugador;
                                    })
                                    .filter(jugador -> !jugador.cartas().isEmpty())
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

    //TODO: refactorizacion
    /*
        entregarCarta(partida);
        filtrarJugadorSinCartas(partida);

        return partida;

    Partida entregarCarta (Partida partida){
        String ganadorId = partida.getRonda().determinarGanador();

        var jugadores = partida.getJugadores().stream()
                .map(jugador -> {
                    if(jugador.id().equals(ganadorId)){
                        jugador.agregarCartas(partida.getRonda().entregarCartas());
                    }
                    return jugador;
                })
                .collect(Collectors.toList());

        partida.setJugadores(jugadores);

        return partida;

    }

    Partida filtrarJugadorSinCartas (Partida partida){
        return partida.toBuilder()
                .jugadores(partida.getJugadores().stream()
                    .filter(jugador -> !jugador.cartas().isEmpty())
                    .collect(Collectors.toList()))
                .build();
    }

     */

    //Conocer el ganador
    // darle las cartas
    // eliminar jugadores sin cartas
    // guardar info ronda
    // Actualizar la partida
}
