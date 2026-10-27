package com.sofkau.usecase.partida.gestionarapuesta;

import com.sofkau.model.partida.Partida;
import com.sofkau.model.ronda.values.Apuesta;
import com.sofkau.usecase.ronda.recibirapuesta.RecibirapuestaUseCase;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class GestionarApuestaUseCase {

    private final RecibirapuestaUseCase recibirapuestaUseCase;

    public Mono<Partida> gestionarApuesta(Partida partida, Apuesta apuesta) {
        final boolean[] cartaEliminada = {false};

        return Flux.fromIterable(partida.getJugadores())
                .map(jugador -> {
                    if (jugador.getId().equals(apuesta.getJugadorId())) {
                        cartaEliminada[0] = Boolean.TRUE.equals(jugador.eliminarCarta(apuesta.getCarta()));
                    }
                    return jugador;
                })
                .collectList()
                .map(lista -> partida.toBuilder().jugadores(lista).build())
                .flatMap(partidaActualizada -> {
                    if (!cartaEliminada[0]) {
                        return Mono.just(partidaActualizada);
                    }
                    return recibirapuestaUseCase
                            .recibirApuesta(partidaActualizada.getRonda().getId(), apuesta)
                            .map(ronda -> partidaActualizada.toBuilder().ronda(ronda).build());
                });
    }
}
