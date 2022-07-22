package com.sofkau.api;

import com.sofkau.model.carta.Carta;
import com.sofkau.model.jugador.Jugador;
import com.sofkau.model.partida.Partida;
import com.sofkau.model.ronda.Ronda;
import com.sofkau.usecase.ronda.recibirapuesta.RecibirapuestaUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class HandlerRonda {

    private final RecibirapuestaUseCase recibirapuestaUseCase;

    public Mono<ServerResponse> PUTRecibirApuesta(ServerRequest serverRequest){
        var id = serverRequest.pathVariable("id");
        return null;
        //serverRequest.bodyToMono(Jugador.class)
                //.map(jugador -> recibirapuestaUseCase.recibirApuesta(id, jugador.id(), jugador.eliminarCarta()))
                //.map(ronda -> recibirapuestaUseCase.recibirApuesta(id, ronda.))
                // .body(partida, Partida.class));
    }
}
