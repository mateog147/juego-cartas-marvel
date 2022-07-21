package com.sofkau.usecase.crearpartida;

import com.sofkau.model.jugador.Jugador;
import com.sofkau.model.jugador.gateways.JugadorRepository;
import com.sofkau.model.partida.Partida;
import com.sofkau.model.partida.gateways.PartidaRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.util.List;

@RequiredArgsConstructor
public class CrearPartidaUseCase {

    private final PartidaRepository repository;
    private final JugadorRepository jugadorRepository;

    public Mono<Partida> crearPartida(List<String> jugadoresIds){

        Partida nuevaPartida = new Partida();
        nuevaPartida.toBuilder()
                .jugadores(jugadores)
                .build();
        return repository.save(nuevaPartida);
    }
}
