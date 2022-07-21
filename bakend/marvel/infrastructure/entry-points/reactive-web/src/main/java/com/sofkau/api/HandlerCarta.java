package com.sofkau.api;

import com.sofkau.model.carta.Carta;
import com.sofkau.usecase.carta.actualizarcarta.ActualizarCartaUseCase;
import com.sofkau.usecase.carta.buscarcarta.BuscarCartaUseCase;
import com.sofkau.usecase.carta.crearcarta.CrearCartaUseCase;
import com.sofkau.usecase.carta.eliminarcarta.EliminarCartaUseCase;
import com.sofkau.usecase.carta.mostrarcartas.MostrarCartasUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class HandlerCarta {

//private final BuscarCartaUseCase buscarCartaUseCase;
private final CrearCartaUseCase crearCartaUseCase;
private final MostrarCartasUseCase mostrarCartasUseCase;
private final EliminarCartaUseCase eliminarCartaUseCase;
private final BuscarCartaUseCase buscarCartaUseCase;

private final ActualizarCartaUseCase actualizarCartaUseCase;


    public Mono<ServerResponse> POSTCrearCartaUseCase(ServerRequest serverRequest) {

        return serverRequest.bodyToMono(Carta.class)
                .flatMap(carta -> crearCartaUseCase.saveCarta(carta))
                .flatMap(cartaGuardada -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
                        .body(Mono.just(cartaGuardada), Carta.class));
    }

    public Mono<ServerResponse> GETMostrarCartasUseCase(ServerRequest serverRequest) {

        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(mostrarCartasUseCase.mostrarCartas(), Carta.class);
    }

    public Mono<ServerResponse> DELETEEliminarCartaUseCase(ServerRequest serverRequest) {

        var id = serverRequest.pathVariable("id");
        return  ServerResponse.ok()
                .body(eliminarCartaUseCase.eliminarCarta(id), Carta.class);
    }

    public  Mono<ServerResponse> GETBuscarCartaPorId(ServerRequest serverRequest){
        var id = serverRequest.pathVariable("id");
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(buscarCartaUseCase.buscarCarta(id), Carta.class);
    }

    public Mono<ServerResponse> PUTModificarPorId(ServerRequest serverRequest) {
        var id = serverRequest.pathVariable("id");

        return serverRequest.bodyToMono(Carta.class)
                .flatMap(carta -> actualizarCartaUseCase.actualziarCarta( id, carta))
                .flatMap(cartaActualizada -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
                        .body(Mono.just(cartaActualizada), Carta.class));
    }
}
