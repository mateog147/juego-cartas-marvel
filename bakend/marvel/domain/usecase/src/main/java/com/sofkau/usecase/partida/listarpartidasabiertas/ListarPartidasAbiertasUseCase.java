package com.sofkau.usecase.partida.listarpartidasabiertas;

import com.sofkau.model.jugador.Jugador;
import com.sofkau.model.partida.Partida;
import com.sofkau.model.partida.gateways.PartidaRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;

@RequiredArgsConstructor
public class ListarPartidasAbiertasUseCase {

    private final PartidaRepository partidaRepository;

    public Flux<Partida> listar(String jugadorId) {
        return partidaRepository.findAll()
                .filter(Partida::estaAbierta)
                .filter(partida -> perteneceAJugador(partida, jugadorId));
    }

    private boolean perteneceAJugador(Partida partida, String jugadorId) {
        if (jugadorId == null || jugadorId.isBlank()) {
            return true;
        }
        if (jugadorId.equals(partida.getHostJugadorId())) {
            return true;
        }
        if (partida.getJugadores() == null) {
            return false;
        }
        return partida.getJugadores().stream().anyMatch(jugador ->
                jugadorId.equals(jugador.getId()) || jugadorId.equals(jugador.getUid()));
    }
}
