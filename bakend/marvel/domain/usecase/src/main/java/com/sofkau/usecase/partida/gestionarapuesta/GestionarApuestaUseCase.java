package com.sofkau.usecase.partida.gestionarapuesta;

import com.sofkau.model.partida.Partida;
import com.sofkau.model.ronda.Apuesta;
import com.sofkau.usecase.ronda.recibirapuesta.RecibirapuestaUseCase;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class GestionarApuestaUseCase {
private final RecibirapuestaUseCase recibirapuestaUseCase;

    public Mono<Partida> gestionarApuesta(Partida partida,Apuesta apuesta){
        return recibirapuestaUseCase.recibirApuesta(partida.getRonda().getId(), apuesta)
                .map(ronda -> partida.toBuilder().ronda(ronda).build());
    }
}
