package com.sofkau.usecase.partida.crearpartida;

import com.sofkau.model.mazo.Mazo;
import com.sofkau.model.partida.Partida;
import com.sofkau.model.partida.gateways.PartidaRepository;
import com.sofkau.model.ronda.Ronda;
import com.sofkau.usecase.carta.mostrarcartas.MostrarCartasUseCase;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.util.ArrayList;

@RequiredArgsConstructor
public class CrearPartidaUseCase {

    private final PartidaRepository repository;
    private final MostrarCartasUseCase mostrarCartasUseCase;

    public Mono<Partida> crearPartida() {
        return mostrarCartasUseCase.mostrarCartas()
                .collectList()
                .flatMap(cartas -> {
                    if (cartas == null || cartas.size() < 10) {
                        return Mono.error(new IllegalStateException(
                                "Se necesitan al menos 10 cartas en Mongo para crear una partida (hay "
                                        + (cartas == null ? 0 : cartas.size()) + ")"));
                    }
                    Mazo mazo = Mazo.getMazo(new ArrayList<>(cartas));
                    return repository.save(Partida.builder()
                            .mazo(mazo)
                            .ronda(new Ronda())
                            .build());
                });
    }
}
