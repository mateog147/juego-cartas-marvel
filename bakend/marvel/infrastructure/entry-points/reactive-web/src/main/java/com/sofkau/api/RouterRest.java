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
public RouterFunction<ServerResponse> routerFunction(HandlerCarta cartaHandler, HandlerJugador jugadorHandler, HandlerPartida partidaHandler, HandlerRonda rondaHandler) {
    return route(GET("/api/carta/"), cartaHandler::GETMostrarCartasUseCase)
    .andRoute(POST("/api/carta/"), cartaHandler::POSTCrearCartaUseCase)
    .andRoute(GET("/api/carta/{id}"), cartaHandler::GETBuscarCartaPorId)
    .andRoute(DELETE("/api/carta/{id}"),cartaHandler::DELETEEliminarCartaUseCase)
    .andRoute(PUT("/api/carta/{id}"),cartaHandler::PUTModificarPorId)
    .andRoute(POST("/api/jugador/"), jugadorHandler::POSTCrearJugador)
    .andRoute(PUT("/api/jugador/puntaje/{id}"), jugadorHandler::PUTActualizarPuntajeJugador)
    .andRoute(GET("/api/jugador/"), jugadorHandler::GETConsultarJugadores)
    .andRoute(PUT("/api/jugador/cartaagregada/{id}"), jugadorHandler::PUTAgregarCartasJugador)
    .andRoute(PUT("/api/jugador/cartaeliminada/{id}"), jugadorHandler::PUTEliminarCartaApostada)
    .andRoute(POST("/api/partida/"), partidaHandler::POSTCrearPartida)
    .andRoute(PUT("/api/ronda/{idronda}/{idjugador}"), rondaHandler::PUTRecibirApuesta)
    .andRoute(GET("/api/ronda/"), rondaHandler::GETConsultarRondas);
    }
}
