package com.sofkau.usecase.ronda.recibirapuesta;

import com.sofkau.model.carta.Carta;
import com.sofkau.model.ronda.Apuesta;
import com.sofkau.model.ronda.Ronda;
import com.sofkau.model.ronda.gateways.RondaRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class RecibirapuestaUseCase {
    private final RondaRepository repository;

    //public Mono<Ronda> recibirApuesta (String rondaId, Carta carta, String usuarioId) {
        public Mono<Ronda> recibirApuesta (String rondaId, Apuesta apuesta) {

        //System.out.println("ronda Id: " + rondaId + "\n" + "usuario Id: " + usuarioId + "carta :" + carta.getNombre());

        /*
        return
                 repository.findById(rondaId)
                         .map(ronda -> {
                             var rnd = ronda.getApuestas().entrySet().stream()
                                     .reduce((acumulador , element ) -> {
                                         if(element.getKey().equalsIgnoreCase(usuarioId)){
                                             element.setValue(carta);
                                             return element;
                                         }else {
                                             element.setValue(carta);
                                             return element;
                                         }
                                     }).get();
                             ronda.setApuestas((Map<String, Carta>) rnd);
                             return ronda;
                         })
                         .flatMap(ronda ->  repository.save(ronda));
         */

            return repository.findById(rondaId)
                    .map(ronda -> {
                        //ronda.setApuestas(apuesta);
                        ronda.setApuestas(List.of(apuesta));
                        //ronda.agregarApuesta(apuesta);
                        return ronda;
                    })
                    .log()

                    .flatMap(repository::save);
        }



}
