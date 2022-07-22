package com.sofkau.usecase.partida.crearpartida;

import com.sofkau.model.jugador.Jugador;
import com.sofkau.model.jugador.gateways.JugadorRepository;
import com.sofkau.model.partida.Partida;
import com.sofkau.model.partida.gateways.PartidaRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class CrearPartidaUseCase {

    private final PartidaRepository repository;
    private final JugadorRepository jugadorRepository;

    public Mono<Partida> crearPartida(List<String> jugadoresIds){
        System.out.printf("jugadores"+jugadoresIds);
        List<Jugador> jugadores = new ArrayList<Jugador> ();

        jugadorRepository.findAllById(jugadoresIds).subscribe(jugador -> jugadores.add(jugador));

        return repository.save(
                new Partida().toBuilder()
                        .jugadores(jugadores)
                        .build());

        }
}
