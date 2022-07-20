package com.sofkau.api;

import com.sofkau.model.jugador.Jugador;
import com.sofkau.usecase.jugador.crearjugador.CrearJugadorUseCase;
import com.sofkau.usecase.jugador.rendirse.RendirseEnRondaUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class HandlerJugador {

    private final CrearJugadorUseCase crearJugadorUseCase;
    private final RendirseEnRondaUseCase rendirseEnRondaUseCase;

    public Mono<ServerResponse> POSTCrearJugador(ServerRequest serverRequest){

        return serverRequest.bodyToMono(Jugador.class)
                .flatMap(item-> crearJugadorUseCase.crearNuevoJugador(item))
                .flatMap(jugador -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
                        .body(Mono.just(jugador), Jugador.class));



    }
}
