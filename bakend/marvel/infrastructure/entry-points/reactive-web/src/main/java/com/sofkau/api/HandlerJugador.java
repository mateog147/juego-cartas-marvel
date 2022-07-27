package com.sofkau.api;

import com.sofkau.model.jugador.Jugador;
import com.sofkau.usecase.partida.crearpartida.agregarcartas.AgregarCartasUseCase;
import com.sofkau.usecase.jugador.consultarjugadores.ConsultarjugadoresUseCase;
import com.sofkau.usecase.jugador.actualizarpuntaje.ActualizarPuntajeJugadorUseCase;
import com.sofkau.usecase.jugador.crearjugador.CrearJugadorUseCase;
import com.sofkau.usecase.jugador.eliminarcartaapostada.EliminarCartaApostadaUseCase;
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
    private final ActualizarPuntajeJugadorUseCase actualizarPuntajeJugadorUseCase;
    private final ConsultarjugadoresUseCase consultarJuegadoresUseCase;
    private final AgregarCartasUseCase agregarCartasUseCase;

    private final EliminarCartaApostadaUseCase eliminarCartaApostadaUseCase;

    public Mono<ServerResponse> POSTCrearJugador(ServerRequest serverRequest){

        return serverRequest.bodyToMono(Jugador.class)
                .flatMap(item-> crearJugadorUseCase.crearNuevoJugador(item))
                .flatMap(jugador -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
                        .body(Mono.just(jugador), Jugador.class));

    }
    public Mono<ServerResponse> PUTActualizarPuntajeJugador(ServerRequest serverRequest){
        var id = serverRequest.pathVariable("id");
        return serverRequest.bodyToMono(Jugador.class)
                .flatMap(item-> actualizarPuntajeJugadorUseCase.actualizarPuntajeDelJugador(id, item.getPuntaje()) )
                .flatMap(jugador -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
                        .body(Mono.just(jugador), Jugador.class));
    }


    public Mono<ServerResponse> GETConsultarJugadores(ServerRequest serverRequest) {
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(consultarJuegadoresUseCase.consultarJugadores(), Jugador.class);
    }


}
