package com.sofkau.api;

import com.sofkau.model.jugador.Jugador;
import com.sofkau.model.partida.Partida;
import com.sofkau.usecase.partida.crearpartida.CrearPartidaUseCase;
import com.sofkau.usecase.partida.guardarpartida.GuardarPartidaUseCase;
import com.sofkau.usecase.partida.repartircartas.RepartirCartasUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class HandlerPartida {

    private final CrearPartidaUseCase crearPartidaUseCase;
    private final RepartirCartasUseCase repartirCartasUseCase;
    private final GuardarPartidaUseCase guardarPartidaUseCase;
    public Mono<ServerResponse> POSTCrearPartida(ServerRequest serverRequest){

        return serverRequest.bodyToFlux(Jugador.class)
                .map(jugador -> jugador.id())
                .collectList()
                .map(lista -> {
                    return crearPartidaUseCase.crearPartida()
                            .flatMap(partida -> repartirCartasUseCase.repartir(partida, lista))
                            .flatMap(partida -> guardarPartidaUseCase.guardarPartida(partida));
                })
                .flatMap(partida -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
                        .body(partida, Partida.class));
    }
}
