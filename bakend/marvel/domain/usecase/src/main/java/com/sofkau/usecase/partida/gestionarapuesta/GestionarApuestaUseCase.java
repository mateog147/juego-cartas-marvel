package com.sofkau.usecase.partida.gestionarapuesta;

import com.sofkau.model.partida.Partida;
import com.sofkau.model.ronda.values.Apuesta;
import com.sofkau.usecase.partida.gestionarganador.GestionarGanadorUseCase;
import com.sofkau.usecase.ronda.recibirapuesta.RecibirapuestaUseCase;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class GestionarApuestaUseCase {
    private final RecibirapuestaUseCase recibirapuestaUseCase;
    private final GestionarGanadorUseCase gestionarGanadorUseCase;
    private Boolean flag = Boolean.FALSE;

    public Mono<Partida> gestionarApuesta(Partida partida,Apuesta apuesta){
        return Flux.fromIterable(partida.getJugadores())
                        .map(jugador -> {
                            if(jugador.getId().equals(apuesta.getJugadorId())){
                                flag= jugador.eliminarCarta(apuesta.getCarta());
                            }
                            return jugador;
                        })
                        .log()
                        .collectList()
                        .map(lista -> partida.toBuilder().jugadores(lista).build())
                        .flatMap(partida1 -> {
                            if(Boolean.TRUE.equals(flag)){
                                return recibirapuestaUseCase.recibirApuesta(partida1.getRonda().getId(), apuesta);
                            }
                            return Mono.just(partida1.getRonda());
                        })
                        .map(ronda -> partida.toBuilder().ronda(ronda).build())
                        /*.flatMap(partida1 -> {
                            if(partida.getJugadores().size()<=partida1.getRonda().getApuestas().size() && partida.getJugadores().size()>1){
                                return gestionarGanadorUseCase.gestionarGanador(partida1);
                            }
                            return Mono.just(partida1);
                        })*/;
    }
}
