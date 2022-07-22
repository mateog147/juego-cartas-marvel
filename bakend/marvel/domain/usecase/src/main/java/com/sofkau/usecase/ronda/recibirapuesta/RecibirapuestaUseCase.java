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

    public Mono<Ronda> recibirApuesta (String rondaId, Carta carta, String usuarioId){

        System.out.println("ronda Id: " + rondaId + "\n" + "usuario Id: "+ usuarioId + "carta :" +carta.getNombre());

        //var nRonda =
        return
                 repository.findById(rondaId)
                         //.map(ronda -> ronda.agregarApuesta(carta, usuarioId));
                         .map(ronda -> new Ronda()
                                 .builder()
                                 .apuestas(ronda.agregarApuesta(carta , usuarioId))
                                 .build())
                         .flatMap(this.repository::save);
                         //.subscribe(ronda -> ronda.agregarApuesta(carta, usuarioId));

        //return repository.save(new Ronda().toBuilder().apuestas(nRonda).build())
    }

}
