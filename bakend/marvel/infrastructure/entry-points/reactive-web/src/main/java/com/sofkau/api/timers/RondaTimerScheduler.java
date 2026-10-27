package com.sofkau.api.timers;

import com.sofkau.api.realtime.PartidaRealtimeHub;
import com.sofkau.model.partida.Partida;
import com.sofkau.usecase.partida.cerrarrondaportiempo.CerrarRondaPorTiempoUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.Disposable;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.concurrent.ConcurrentHashMap;

@Component
@RequiredArgsConstructor
public class RondaTimerScheduler {

    private final CerrarRondaPorTiempoUseCase cerrarRondaPorTiempoUseCase;
    private final PartidaRealtimeHub partidaRealtimeHub;
    private final ConcurrentHashMap<String, Disposable> jobs = new ConcurrentHashMap<>();

    public void programar(Partida partida) {
        if (partida == null
                || partida.getId() == null
                || partida.getRonda() == null
                || partida.getRonda().getId() == null
                || partida.getRonda().getFinRondaEn() == null) {
            return;
        }

        String key = jobKey(partida.getId(), partida.getRonda().getId(), partida.getRonda().getNumero());
        Disposable previous = jobs.remove(key);
        if (previous != null && !previous.isDisposed()) {
            previous.dispose();
        }

        long delayMs = Math.max(0L, partida.getRonda().getFinRondaEn() - System.currentTimeMillis());
        String partidaId = partida.getId();
        String rondaId = partida.getRonda().getId();
        Integer numero = partida.getRonda().getNumero();

        Disposable disposable = Mono.delay(Duration.ofMillis(delayMs))
                .flatMap(tick -> cerrarRondaPorTiempoUseCase.cerrar(partidaId, rondaId, numero))
                .doOnNext(this::afterTimeout)
                .doFinally(signal -> jobs.remove(key))
                .subscribe(
                        ok -> {
                        },
                        err -> System.err.println("Error en timer de ronda " + key + ": " + err.getMessage())
                );

        jobs.put(key, disposable);
    }

    private void afterTimeout(Partida partida) {
        if (esRondaResuelta(partida)) {
            partidaRealtimeHub.emitRoundResolved(partida);
        } else {
            partidaRealtimeHub.emitBetPlaced(partida);
        }
        if (partida.getRonda() != null
                && partida.getRonda().getFinRondaEn() != null
                && partida.getJugadores() != null
                && partida.getJugadores().size() >= 2) {
            programar(partida);
        }
    }

    private boolean esRondaResuelta(Partida partida) {
        return partida.getRonda() != null
                && (partida.getRonda().getApuestas() == null || partida.getRonda().getApuestas().isEmpty())
                && partida.getRonda().getUltimoGanador() != null
                && !partida.getRonda().getUltimoGanador().isBlank();
    }

    private static String jobKey(String partidaId, String rondaId, Integer numero) {
        return partidaId + ":" + rondaId + ":" + numero;
    }
}
