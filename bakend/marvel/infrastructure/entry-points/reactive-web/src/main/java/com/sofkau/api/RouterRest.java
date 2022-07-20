package com.sofkau.api;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.RequestPredicates.*;


@Configuration
public class RouterRest {
@Bean
public RouterFunction<ServerResponse> routerFunction(HandlerCarta handler, HandlerJugador jugador) {
    return route(GET("/api/carta/"), handler::GETMostrarCartasUseCase)
    .andRoute(POST("/api/carta/crear"), handler::POSTCrearCartaUseCase)
    .andRoute(POST("/api/jugador/crear"), jugador::POSTCrearJugador)
    .andRoute(PUT("api/jugador/puntaje/{id}"), jugador::PUTActualizarPuntajeJugador);
            //.and(route(GET("/api/otherusercase/path"), handler::listenGETOtherUseCase));

    }
}
