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
    .andRoute(POST("/api/partida/"), partidaHandler::POSTCrearPartida)
    .andRoute(GET("/api/partida/{id}"), partidaHandler::GETPartidaPorId)
    .andRoute(POST("/api/partida/{id}"), partidaHandler::PUTNuevaApuesta)
    .andRoute(POST("/api/partida/ganador/{id}"), partidaHandler::POSTGanadorRonda)
    .andRoute(POST("/api/partida/rendirse/{id}"), partidaHandler::POSTRetirarJugador);
    }
}
