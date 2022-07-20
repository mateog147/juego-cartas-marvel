package com.sofkau.api;

import com.sofkau.model.carta.Carta;
import com.sofkau.usecase.buscarcarta.BuscarCartaUseCase;
import com.sofkau.usecase.crearcarta.CrearCartaUseCase;
import com.sofkau.usecase.eliminarcarta.EliminarCartaUseCase;
import com.sofkau.usecase.mostrarcartas.MostrarCartasUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class HandlerCarta {

//private final BuscarCartaUseCase buscarCartaUseCase;
private final CrearCartaUseCase crearCartaUseCase;
private final MostrarCartasUseCase mostrarCartasUseCase;
private final EliminarCartaUseCase eliminarCartaUseCase;


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
}
