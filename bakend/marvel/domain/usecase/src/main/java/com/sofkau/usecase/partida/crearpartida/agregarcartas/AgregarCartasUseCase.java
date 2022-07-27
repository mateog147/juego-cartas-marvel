package com.sofkau.usecase.jugador.agregarcartas;

import com.sofkau.model.carta.Carta;
import com.sofkau.model.jugador.Jugador;
import com.sofkau.model.jugador.gateways.JugadorRepository;
import com.sofkau.model.partida.Partida;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@RequiredArgsConstructor
public class AgregarCartasUseCase {
    private final JugadorRepository repository;

    /*
    public Mono<Jugador> agregarCartas(Partida partida){

        String ganadorId = partida.getRonda().determinarGanador();
        return  Flux.fromIterable(partida.getJugadores())
                .map(jugador -> {
                    if(jugador.id().equals(ganadorId)){
                        nombreGanador = jugador.getNombre();
                        jugador.agregarCartas(partida.getRonda().entregarCartas());
                    }
                    return jugador;
                })
     */

    public Mono<Jugador> agregarCartas(String jugadorId, List<Carta> cartasNuevas){

        return repository.findById(jugadorId)
                .map(jugador -> jugador.toBuilder()
                        .cartas(jugador.agregarCartas(cartasNuevas))
                        .build()
                );

        }

    }


