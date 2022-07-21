package com.sofkau.usecase.crearpartida;

import com.sofkau.model.jugador.Jugador;
import com.sofkau.model.jugador.gateways.JugadorRepository;
import com.sofkau.model.partida.Partida;
import com.sofkau.model.partida.gateways.PartidaRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class CrearPartidaUseCase {

    private final PartidaRepository repository;
    private final JugadorRepository jugadorRepository;

    public Mono<Partida> crearPartida(List<String> jugadoresIds){
        Partida nuevaPartida = new Partida();
        List<Jugador> jugadores = new ArrayList<>();
        System.out.println("Los ides son:" + jugadoresIds.toString());
        jugadorRepository.findAllById(jugadoresIds)
                .log()
                .map(j -> jugadores.add(j))
                .then();

        return repository.save(nuevaPartida.toBuilder()
                .jugadores(jugadores).build());
    }
}
