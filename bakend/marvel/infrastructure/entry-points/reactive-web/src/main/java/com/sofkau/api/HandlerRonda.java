package com.sofkau.api;

import com.sofkau.model.carta.Carta;
import com.sofkau.model.jugador.Jugador;
import com.sofkau.model.partida.Partida;
import com.sofkau.model.ronda.Apuesta;
import com.sofkau.model.ronda.Ronda;
import com.sofkau.model.ronda.gateways.RondaRepository;
import com.sofkau.usecase.ronda.consultarrondas.ConsultarRondasUseCase;
import com.sofkau.usecase.ronda.crearronda.CrearRondaUseCase;
import com.sofkau.usecase.ronda.ganadorronda.GanadorRondaUseCase;
import com.sofkau.usecase.ronda.recibirapuesta.RecibirapuestaUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class HandlerRonda {

    private final RecibirapuestaUseCase recibirapuestaUseCase;
    private final ConsultarRondasUseCase consultarRondasUseCase;
    private final CrearRondaUseCase crearRondaUseCase;

    private final GanadorRondaUseCase ganadorRondaUseCase;


    public Mono<ServerResponse> PUTRecibirApuesta(ServerRequest serverRequest){
        var idRonda = serverRequest.pathVariable("id");

        return serverRequest.bodyToMono(Apuesta.class)
                .log()
                .flatMap(apuesta -> recibirapuestaUseCase.recibirApuesta(idRonda, apuesta))
                .flatMap(ronda -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(Mono.just(ronda), Ronda.class));

    }

    public Mono<ServerResponse> GETConsultarRondas(ServerRequest serverRequest) {
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(consultarRondasUseCase.consultarRondas(), Ronda.class);
    }

    public Mono<ServerResponse> POSTCrearRonda(ServerRequest serverRequest){
        return serverRequest.bodyToMono(Ronda.class)
                .flatMap(item-> crearRondaUseCase.crearRonda(item))
                .flatMap(ronda -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
                        .body(Mono.just(ronda), Ronda.class));
    }

    /*public Mono<ServerResponse> GETGanadorRonda (ServerRequest serverRequest){
        var idRonda = serverRequest.pathVariable("id");
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(ganadorRondaUseCase.determinarGanadaorRonda(idRonda), Ronda.class);
    }*/
}
