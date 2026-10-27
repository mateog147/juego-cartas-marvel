package com.sofkau.usecase.partida.cerrarrondaportiempo;

import com.sofkau.model.carta.Carta;
import com.sofkau.model.jugador.Jugador;
import com.sofkau.model.partida.Partida;
import com.sofkau.model.ronda.values.Apuesta;
import com.sofkau.usecase.partida.encontrarpartida.EncontrarPartidaUseCase;
import com.sofkau.usecase.partida.gestionarapuesta.GestionarApuestaUseCase;
import com.sofkau.usecase.partida.gestionarganador.GestionarGanadorUseCase;
import com.sofkau.usecase.partida.guardarpartida.GuardarPartidaUseCase;
import com.sofkau.usecase.partida.verificarganadorpartida.VerificarGanadorPartidaUseCase;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class CerrarRondaPorTiempoUseCase {

    private final EncontrarPartidaUseCase encontrarPartidaUseCase;
    private final GestionarApuestaUseCase gestionarApuestaUseCase;
    private final GestionarGanadorUseCase gestionarGanadorUseCase;
    private final VerificarGanadorPartidaUseCase verificarGanadorPartidaUseCase;
    private final GuardarPartidaUseCase guardarPartidaUseCase;

    /**
     * When the round timer expires: auto-bet a random card for players who have not bet yet,
     * then resolve the round if there are enough bets.
     */
    public Mono<Partida> cerrar(String partidaId, String rondaId, Integer numeroEsperado) {
        return encontrarPartidaUseCase.encontrarPartida(partidaId)
                .flatMap(partida -> {
                    if (!partida.permiteJugar()) {
                        return Mono.just(partida);
                    }
                    if (partida.getRonda() == null
                            || !Objects.equals(partida.getRonda().getId(), rondaId)
                            || !Objects.equals(partida.getRonda().getNumero(), numeroEsperado)) {
                        // Stale timer from a previous round — ignore
                        return Mono.just(partida);
                    }

                    Set<String> yaApostaron = partida.getRonda().getApuestas() == null
                            ? new HashSet<>()
                            : partida.getRonda().getApuestas().stream()
                            .map(Apuesta::getJugadorId)
                            .collect(Collectors.toSet());

                    List<Jugador> pendientes = partida.getJugadores().stream()
                            .filter(j -> j.getId() != null && !yaApostaron.contains(j.getId()))
                            .filter(j -> j.getCartas() != null && !j.getCartas().isEmpty())
                            .collect(Collectors.toList());

                    AtomicReference<Partida> ref = new AtomicReference<>(partida);

                    return Flux.fromIterable(pendientes)
                            .concatMap(jugadorPlantilla -> {
                                Partida actual = ref.get();
                                Jugador jugador = actual.getJugadores().stream()
                                        .filter(j -> Objects.equals(j.getId(), jugadorPlantilla.getId()))
                                        .findFirst()
                                        .orElse(null);
                                if (jugador == null || jugador.getCartas() == null || jugador.getCartas().isEmpty()) {
                                    return Mono.empty();
                                }
                                boolean ya = actual.getRonda().getApuestas() != null
                                        && actual.getRonda().getApuestas().stream()
                                        .anyMatch(a -> Objects.equals(a.getJugadorId(), jugador.getId()));
                                if (ya) {
                                    return Mono.empty();
                                }
                                Carta carta = elegirCarta(jugador.getCartas());
                                Apuesta apuesta = new Apuesta(jugador.getId(), carta);
                                return gestionarApuestaUseCase.gestionarApuesta(actual, apuesta)
                                        .doOnNext(ref::set);
                            })
                            .then(Mono.defer(() -> Mono.just(ref.get())))
                            .flatMap(this::resolverSiHayApuestasSuficientes)
                            .flatMap(guardarPartidaUseCase::guardarPartida);
                });
    }

    private Carta elegirCarta(List<Carta> cartas) {
        return cartas.get(ThreadLocalRandom.current().nextInt(cartas.size()));
    }

    private Mono<Partida> resolverSiHayApuestasSuficientes(Partida partida) {
        int jugadores = partida.getJugadores() == null ? 0 : partida.getJugadores().size();
        int apuestas = partida.getRonda() == null || partida.getRonda().getApuestas() == null
                ? 0
                : partida.getRonda().getApuestas().size();

        // determinarGanador requires more than one bet
        if (jugadores >= 2 && apuestas >= 2 && apuestas >= jugadores) {
            return gestionarGanadorUseCase.gestionarGanador(partida)
                    .flatMap(verificarGanadorPartidaUseCase::verificarGanadorPartida);
        }
        return Mono.just(partida);
    }
}
