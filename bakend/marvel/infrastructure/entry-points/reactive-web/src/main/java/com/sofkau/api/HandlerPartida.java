package com.sofkau.api;

import com.sofkau.model.jugador.Jugador;
import com.sofkau.model.partida.Partida;
import com.sofkau.model.ronda.values.Apuesta;
import com.sofkau.usecase.partida.agregarronda.AgregarRondaUseCase;
import com.sofkau.usecase.partida.cerrarpartida.CerrarPartidaUseCase;
import com.sofkau.usecase.partida.crearpartida.CrearPartidaUseCase;
import com.sofkau.usecase.partida.encontrarpartida.EncontrarPartidaUseCase;
import com.sofkau.usecase.partida.gestionarapuesta.GestionarApuestaUseCase;
import com.sofkau.usecase.partida.gestionarganador.GestionarGanadorUseCase;
import com.sofkau.usecase.partida.guardarpartida.GuardarPartidaUseCase;
import com.sofkau.usecase.partida.iniciarpartida.IniciarPartidaUseCase;
import com.sofkau.usecase.partida.listarpartidasabiertas.ListarPartidasAbiertasUseCase;
import com.sofkau.usecase.partida.repartircartas.RepartirCartasUseCase;
import com.sofkau.usecase.partida.retirarjugador.RetirarJugadorUseCase;
import com.sofkau.usecase.partida.verificarganadorpartida.VerificarGanadorPartidaUseCase;
import com.sofkau.api.realtime.PartidaRealtimeHub;
import com.sofkau.api.timers.RondaTimerScheduler;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
    private final RetirarJugadorUseCase retirarJugadorUseCase;
    private final VerificarGanadorPartidaUseCase verificarGanadorPartidaUseCase;
    private final ListarPartidasAbiertasUseCase listarPartidasAbiertasUseCase;
    private final CerrarPartidaUseCase cerrarPartidaUseCase;
    private final IniciarPartidaUseCase iniciarPartidaUseCase;
    private final RondaTimerScheduler rondaTimerScheduler;
    private final PartidaRealtimeHub partidaRealtimeHub;

    public Mono<ServerResponse> POSTCrearPartida(ServerRequest serverRequest){

        return serverRequest.bodyToFlux(Jugador.class)
                .map(jugador -> jugador.id())
                .collectList()
                .flatMap(lista -> crearPartidaUseCase.crearPartida()
                        .flatMap(partida -> repartirCartasUseCase.repartir(partida, lista))
                        .flatMap(partida -> agregarRondaUseCase.agregarRonda(partida, false))
                        .map(partida -> partida.toBuilder()
                                .estado(Partida.ESTADO_ESPERANDO)
                                .hostJugadorId(lista.isEmpty() ? null : lista.get(0))
                                .creadaEn(partida.getCreadaEn() != null
                                        ? partida.getCreadaEn()
                                        : System.currentTimeMillis())
                                .build())
                        .flatMap(partida -> guardarPartidaUseCase.guardarPartida(partida)))
                .doOnNext(partidaRealtimeHub::emitState)
                .flatMap(partida -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(partida))
                .onErrorResume(error -> {
                    System.err.println("Error creando partida: " + error.getMessage());
                    error.printStackTrace();
                    return ServerResponse.badRequest()
                            .contentType(MediaType.APPLICATION_JSON)
                            .bodyValue(errorJson(error.getMessage()));
                });
    }

    public Mono<ServerResponse> GETPartidasAbiertas(ServerRequest serverRequest) {
        String jugadorId = serverRequest.queryParam("jugadorId").orElse("");
        return listarPartidasAbiertasUseCase.listar(jugadorId)
                .collectList()
                .flatMap(partidas -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(partidas));
    }

    public Mono<ServerResponse> POSTIniciarPartida(ServerRequest serverRequest) {
        var id = serverRequest.pathVariable("id");
        String jugadorId = callerId(serverRequest);
        if (jugadorId.isBlank()) {
            return badRequest("Falta jugadorId");
        }
        return iniciarPartidaUseCase.iniciar(id, jugadorId)
                .doOnNext(partida -> {
                    rondaTimerScheduler.programar(partida);
                    partidaRealtimeHub.emitPartidaIniciada(partida);
                })
                .flatMap(partida -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(partida))
                .onErrorResume(IllegalArgumentException.class, e -> badRequest(e.getMessage()))
                .onErrorResume(IllegalStateException.class, e -> forbidden(e.getMessage()));
    }

    public Mono<ServerResponse> POSTCerrarPartida(ServerRequest serverRequest) {
        var id = serverRequest.pathVariable("id");
        String jugadorId = callerId(serverRequest);
        if (jugadorId.isBlank()) {
            return badRequest("Falta jugadorId");
        }
        return encontrarPartidaUseCase.encontrarPartida(id)
                .flatMap(partida -> {
                    if (!partida.esMiembro(jugadorId)) {
                        return Mono.error(new IllegalStateException("No sos miembro de esta mesa"));
                    }
                    return cerrarPartidaUseCase.cerrar(id);
                })
                .doOnNext(this::afterPartidaCerrada)
                .flatMap(partida -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(partida))
                .onErrorResume(IllegalArgumentException.class, e -> badRequest(e.getMessage()))
                .onErrorResume(IllegalStateException.class, e -> forbidden(e.getMessage()));
    }

    public Mono<ServerResponse> GETPartidaPorId(ServerRequest serverRequest){
        var id = serverRequest.pathVariable("id");
        String jugadorId = callerId(serverRequest);
        if (jugadorId.isBlank()) {
            return badRequest("Falta jugadorId");
        }
        return encontrarPartidaUseCase.encontrarPartida(id)
                .flatMap(partida -> {
                    if (!partida.esMiembro(jugadorId)) {
                        return Mono.error(new IllegalStateException("No sos miembro de esta mesa"));
                    }
                    return Mono.just(partida);
                })
                .flatMap(partida -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(partida))
                .onErrorResume(IllegalStateException.class, e -> forbidden(e.getMessage()))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> POSTNuevaApuesta(ServerRequest serverRequest){
        var idPartida = serverRequest.pathVariable("id");
        String caller = callerId(serverRequest);

        return serverRequest.bodyToMono(Apuesta.class)
                .flatMap(apuesta -> encontrarPartidaUseCase.encontrarPartida(idPartida)
                        .flatMap(partida -> {
                            String jugadorId = !caller.isBlank() ? caller : apuesta.getJugadorId();
                            if (!partida.esMiembro(jugadorId)) {
                                return Mono.error(new IllegalStateException("No sos miembro de esta mesa"));
                            }
                            if (!partida.permiteJugar()) {
                                return Mono.error(new IllegalStateException(
                                        partida.estaEsperando()
                                                ? "La mesa todavía no empezó"
                                                : "La mesa ya está cerrada"));
                            }
                            return gestionarApuestaUseCase.gestionarApuesta(partida, apuesta);
                        })
                        .flatMap(this::resolverRondaSiCompleta)
                        .flatMap(partida -> guardarPartidaUseCase.guardarPartida(partida)))
                .doOnNext(this::afterBetMutation)
                .flatMap(partida -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(partida))
                .onErrorResume(IllegalStateException.class, e ->
                        e.getMessage() != null && e.getMessage().contains("miembro")
                                ? forbidden(e.getMessage())
                                : badRequest(e.getMessage()));
    }

    private void afterBetMutation(Partida partida) {
        if (!partida.estaAbierta()) {
            afterPartidaCerrada(partida);
            return;
        }
        if (!partida.permiteJugar()) {
            partidaRealtimeHub.emitState(partida);
            return;
        }
        rondaTimerScheduler.programar(partida);
        if (esRondaResuelta(partida)) {
            partidaRealtimeHub.emitRoundResolved(partida);
        } else {
            partidaRealtimeHub.emitBetPlaced(partida);
        }
    }

    private void afterPartidaCerrada(Partida partida) {
        rondaTimerScheduler.cancelar(partida.getId());
        partidaRealtimeHub.emitPartidaCerrada(partida);
    }

    private boolean esRondaResuelta(Partida partida) {
        return partida.getRonda() != null
                && (partida.getRonda().getApuestas() == null || partida.getRonda().getApuestas().isEmpty())
                && partida.getRonda().getUltimoGanador() != null
                && !partida.getRonda().getUltimoGanador().isBlank();
    }

    private Mono<Partida> resolverRondaSiCompleta(Partida partida) {
        int jugadores = partida.getJugadores() == null ? 0 : partida.getJugadores().size();
        int apuestas = partida.getRonda() == null || partida.getRonda().getApuestas() == null
                ? 0
                : partida.getRonda().getApuestas().size();

        if (jugadores >= 2 && apuestas >= jugadores) {
            return gestionarGanadorUseCase.gestionarGanador(partida)
                    .flatMap(verificarGanadorPartidaUseCase::verificarGanadorPartida);
        }
        return Mono.just(partida);
    }

    public Mono<ServerResponse> GETGanadorRonda(ServerRequest serverRequest) {
        var id = serverRequest.pathVariable("id");
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(encontrarPartidaUseCase.encontrarPartida(id)
                        .flatMap(partida -> {
                            if (!partida.permiteJugar()) {
                                return Mono.just(partida);
                            }
                            return gestionarGanadorUseCase.gestionarGanador(partida)
                                    .flatMap(verificarGanadorPartidaUseCase::verificarGanadorPartida)
                                    .flatMap(guardarPartidaUseCase::guardarPartida);
                        })
                        .doOnNext(partida -> {
                            if (!partida.estaAbierta()) {
                                afterPartidaCerrada(partida);
                            } else {
                                afterBetMutation(partida);
                            }
                        })
                , Partida.class);
    }

    public Mono<ServerResponse> POSTRetirarJugador(ServerRequest serverRequest) {
        var id = serverRequest.pathVariable("id");
        return serverRequest.bodyToMono(Jugador.class)
                .map(jugador -> jugador.id())
                .flatMap(jugadorId -> encontrarPartidaUseCase.encontrarPartida(id)
                        .flatMap(partida -> {
                            if (!partida.esMiembro(jugadorId)) {
                                return Mono.error(new IllegalStateException("No sos miembro de esta mesa"));
                            }
                            if (!partida.estaAbierta()) {
                                return Mono.just(partida);
                            }
                            if (partida.estaEsperando()) {
                                return Mono.error(new IllegalStateException("La mesa todavía no empezó"));
                            }
                            return retirarJugadorUseCase.retirarJugador(partida, jugadorId)
                                    .flatMap(verificarGanadorPartidaUseCase::verificarGanadorPartida)
                                    .flatMap(guardarPartidaUseCase::guardarPartida);
                        }))
                .doOnNext(partida -> {
                    if (!partida.estaAbierta()) {
                        afterPartidaCerrada(partida);
                    } else {
                        partidaRealtimeHub.emitState(partida);
                    }
                })
                .flatMap(partida -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(partida))
                .onErrorResume(IllegalStateException.class, e ->
                        e.getMessage() != null && e.getMessage().contains("miembro")
                                ? forbidden(e.getMessage())
                                : badRequest(e.getMessage()));
    }

    private String callerId(ServerRequest request) {
        return request.queryParam("jugadorId").orElse("").trim();
    }

    private Mono<ServerResponse> badRequest(String message) {
        return ServerResponse.badRequest()
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(errorJson(message));
    }

    private Mono<ServerResponse> forbidden(String message) {
        return ServerResponse.status(HttpStatus.FORBIDDEN)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(errorJson(message));
    }

    private String errorJson(String message) {
        String safe = message == null ? "Error" : message.replace("\"", "'");
        return "{\"error\":\"" + safe + "\"}";
    }
}
