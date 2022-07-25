package com.sofkau.api;

import com.sofkau.model.jugador.Jugador;
import com.sofkau.model.partida.Partida;
import com.sofkau.model.ronda.values.Apuesta;
import com.sofkau.model.ronda.Ronda;
import com.sofkau.usecase.partida.agregarronda.AgregarRondaUseCase;
import com.sofkau.usecase.partida.crearpartida.CrearPartidaUseCase;
import com.sofkau.usecase.partida.encontrarpartida.EncontrarPartidaUseCase;
import com.sofkau.usecase.partida.gestionarapuesta.GestionarApuestaUseCase;
import com.sofkau.usecase.partida.gestionarganador.GestionarGanadorUseCase;
import com.sofkau.usecase.partida.guardarpartida.GuardarPartidaUseCase;
import com.sofkau.usecase.partida.repartircartas.RepartirCartasUseCase;
import com.sofkau.usecase.partida.retirarjugador.RetirarJugadorUseCase;
import com.sofkau.usecase.partida.verificarganadorpartida.VerificarGanadorPartidaUseCase;
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
    private final AgregarRondaUseCase agregarRondaUseCase;
    private final GuardarPartidaUseCase guardarPartidaUseCase;
    private final GestionarApuestaUseCase gestionarApuestaUseCase;
    private final EncontrarPartidaUseCase encontrarPartidaUseCase;
    private final GestionarGanadorUseCase gestionarGanadorUseCase;
    private  final RetirarJugadorUseCase retirarJugadorUseCase;
    private final VerificarGanadorPartidaUseCase verificarGanadorPartidaUseCase;

    public Mono<ServerResponse> POSTCrearPartida(ServerRequest serverRequest){
        return serverRequest.bodyToFlux(Jugador.class)
                .map(jugador -> jugador.id())
                .collectList()
                .map(lista -> {
                    return crearPartidaUseCase.crearPartida()
                            .flatMap(partida -> repartirCartasUseCase.repartir(partida, lista))
                            .flatMap(partida -> agregarRondaUseCase.agregarRonda(partida))
                            .flatMap(partida -> guardarPartidaUseCase.guardarPartida(partida));
                })
                .flatMap(partida -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
                        .body(partida, Partida.class));
    }

    public Mono<ServerResponse>GETPartidaPorId(ServerRequest serverRequest){
        var id = serverRequest.pathVariable("id");
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(encontrarPartidaUseCase.encontrarPartida(id), Partida.class);
    }

    public Mono<ServerResponse> POSTNuevaApuesta(ServerRequest serverRequest){
        var idPartida = serverRequest.pathVariable("id");


        return serverRequest.bodyToMono(Apuesta.class)
                .flatMap(apuesta -> {
                            return encontrarPartidaUseCase.encontrarPartida(idPartida)
                                    .flatMap(partida -> gestionarApuestaUseCase.gestionarApuesta(partida, apuesta))
                                    .flatMap(partida -> verificarGanadorPartidaUseCase.verificarGanadorPartida(partida))
                                    .flatMap(partida -> guardarPartidaUseCase.guardarPartida(partida));
                        })
                .flatMap(ronda -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(Mono.just(ronda), Ronda.class));
    }

    public Mono<ServerResponse> GETGanadorRonda(ServerRequest serverRequest) {
        var id = serverRequest.pathVariable("id");
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(encontrarPartidaUseCase.encontrarPartida(id)
                        .flatMap(partida -> gestionarGanadorUseCase.gestionarGanador(partida))
                        .flatMap(partida -> verificarGanadorPartidaUseCase.verificarGanadorPartida(partida))
                        .flatMap(partida -> guardarPartidaUseCase.guardarPartida(partida))
                , Partida.class);
    }

    public Mono<ServerResponse> POSTRetirarJugador(ServerRequest serverRequest) {
        var id = serverRequest.pathVariable("id");
        return serverRequest.bodyToMono(Jugador.class)
                .map(jugador -> jugador.id())
                .map(jugadorId -> {
                    return encontrarPartidaUseCase.encontrarPartida(id)
                            .flatMap(partida -> retirarJugadorUseCase.retirarJugador(partida, jugadorId))
                            .flatMap(partida -> guardarPartidaUseCase.guardarPartida(partida));
                })
                .flatMap(partida -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
                        .body(partida, Partida.class));
    }
}
