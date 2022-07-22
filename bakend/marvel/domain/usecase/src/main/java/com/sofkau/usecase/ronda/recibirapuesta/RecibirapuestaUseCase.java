package com.sofkau.usecase.ronda.recibirapuesta;

import com.sofkau.model.carta.Carta;
import com.sofkau.model.ronda.Ronda;
import com.sofkau.model.ronda.gateways.RondaRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.util.Map;

@RequiredArgsConstructor
public class RecibirapuestaUseCase {
    private final RondaRepository repository;

    public Mono<Ronda> recibirApuesta ( String rondaId, Carta carta, String usuarioId){

        Ronda nuevaRonda = repository.findById(rondaId)
                .block();

        nuevaRonda.agregarApuesta(carta, usuarioId);

        return repository.save(nuevaRonda);

    }
}
