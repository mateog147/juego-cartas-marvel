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
public RouterFunction<ServerResponse> routerFunction(HandlerCarta handler, HandlerJugador jugador, HandlerPartida partidaHandler) {
    return route(GET("/api/carta/"), handler::GETMostrarCartasUseCase)
    .andRoute(POST("/api/carta/"), handler::POSTCrearCartaUseCase)
    .andRoute(GET("/api/carta/{id}"), handler::GETBuscarCartaPorId)
    .andRoute(DELETE("/api/carta/{id}"),handler::DELETEEliminarCartaUseCase)
    .andRoute(PUT("/api/carta/{id}"),handler::PUTModificarPorId)
    .andRoute(POST("/api/jugador/"), jugador::POSTCrearJugador)
    .andRoute(PUT("/api/jugador/puntaje/{id}"), jugador::PUTActualizarPuntajeJugador)
    .andRoute(GET("/api/jugador/"), jugador::GETConsultarJugadores)
    .andRoute(PUT("/api/jugador/cartaagregada/{id}"), jugador::PUTAgregarCartasJugador)
    .andRoute(PUT("/api/jugador/cartaeliminada/{id}"), jugador::PUTEliminarCartaApostada)
    .andRoute(POST("/api/partida/"), partidaHandler::POSTCrearPartida);
    }
}
