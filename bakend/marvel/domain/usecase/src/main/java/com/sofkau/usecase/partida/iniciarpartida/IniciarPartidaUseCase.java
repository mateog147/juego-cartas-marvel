package com.sofkau.usecase.partida.iniciarpartida;

import com.sofkau.model.partida.Partida;
import com.sofkau.model.partida.gateways.PartidaRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class IniciarPartidaUseCase {

    private final PartidaRepository partidaRepository;

    public Mono<Partida> iniciar(String partidaId, String hostJugadorId) {
        return partidaRepository.findById(partidaId)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Partida no encontrada: " + partidaId)))
                .flatMap(partida -> {
                    if (!partida.esHost(hostJugadorId)) {
                        return Mono.error(new IllegalStateException("Solo el host puede empezar la partida"));
                    }
                    if (partida.estaEnJuego()) {
                        return Mono.just(partida);
                    }
                    if (!partida.estaEsperando()) {
                        return Mono.error(new IllegalStateException("La mesa no se puede iniciar en estado: " + partida.getEstado()));
                    }
                    if (partida.getRonda() == null) {
                        return Mono.error(new IllegalStateException("La partida no tiene ronda inicial"));
                    }
                    partida.marcarEnJuego();
                    partida.getRonda().iniciarTemporizador();
                    return partidaRepository.save(partida);
                });
    }
}
