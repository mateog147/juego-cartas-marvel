package com.sofkau.api;

import com.sofkau.model.carta.Carta;
import com.sofkau.model.jugador.Jugador;
import com.sofkau.model.partida.Partida;
import com.sofkau.model.ronda.Ronda;
import com.sofkau.usecase.ronda.consultarrondas.ConsultarRondasUseCase;
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
    private final ConsultarRondasUseCase consultarRondasUseCase;

    public Mono<ServerResponse> PUTRecibirApuesta(ServerRequest serverRequest){
        var idRonda = serverRequest.pathVariable("idronda");
        var idJugador = serverRequest.pathVariable("idjugador");

        System.out.println("id: " + idJugador + "\n" + idRonda);

        return serverRequest.bodyToMono(Carta.class)
                .map(carta -> recibirapuestaUseCase.recibirApuesta(idRonda, carta, idJugador))
                .flatMap(ronda -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
                        .body(ronda, Ronda.class));
    }

    public Mono<ServerResponse> GETConsultarRondas(ServerRequest serverRequest) {
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(consultarRondasUseCase.consultarRondas(), Ronda.class);
    }
}
