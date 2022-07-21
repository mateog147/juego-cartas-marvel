package com.sofkau.api;

import com.sofkau.model.jugador.Jugador;
import com.sofkau.model.partida.Partida;
import com.sofkau.usecase.crearpartida.CrearPartidaUseCase;
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

    public Mono<ServerResponse> POSTCrearPartida(ServerRequest serverRequest){

        return serverRequest.bodyToFlux(Jugador.class)
                .map(jugador -> jugador.getId())
                .log()
                .collectList()
                .map(lista -> crearPartidaUseCase.crearPartida(lista))
                .flatMap(partida -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
                        .body(partida, Partida.class));

    }
}
