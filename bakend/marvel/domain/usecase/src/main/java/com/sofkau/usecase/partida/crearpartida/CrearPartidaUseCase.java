package com.sofkau.usecase.partida.crearpartida;

import com.sofkau.model.jugador.Jugador;
import com.sofkau.model.jugador.gateways.JugadorRepository;
import com.sofkau.model.partida.Partida;
import com.sofkau.model.partida.gateways.PartidaRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class CrearPartidaUseCase {

    private final PartidaRepository repository;
    private final JugadorRepository jugadorRepository;

    public Mono<Partida> crearPartida(List<String> jugadoresIds){

        List<Jugador> jugadores = new ArrayList<Jugador>();

         var jugador = jugadoresIds.stream()
                .map(jugadorId -> jugadorRepository.findById(jugadorId))
                 .map(i -> i.block())
                .collect(Collectors.toList());

        Partida nuevaPartida = new Partida();
        nuevaPartida.toBuilder()
                .jugadores(jugador)
                .build();

        System.out.printf("hola"+jugadoresIds);
        return repository.save(nuevaPartida);
    }
}
