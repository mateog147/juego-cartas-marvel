package com.sofkau.usecase.partida.cerrarpartida;

import com.sofkau.model.partida.Partida;
import com.sofkau.model.partida.gateways.PartidaRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class CerrarPartidaUseCase {

    private final PartidaRepository partidaRepository;

    public Mono<Partida> cerrar(String partidaId, String ganadorJugadorId) {
        return partidaRepository.findById(partidaId)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Partida no encontrada: " + partidaId)))
                .flatMap(partida -> {
                    if (!partida.estaAbierta()) {
                        return Mono.just(partida);
                    }
                    partida.marcarCerrada(ganadorJugadorId);
                    return partidaRepository.save(partida);
                });
    }

    public Mono<Partida> cerrar(String partidaId) {
        return cerrar(partidaId, null);
    }
}
