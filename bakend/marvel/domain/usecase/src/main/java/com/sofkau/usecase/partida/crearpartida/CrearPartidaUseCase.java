package com.sofkau.usecase.partida.crearpartida;

import com.sofkau.model.carta.Carta;
import com.sofkau.model.mazo.Mazo;
import com.sofkau.model.partida.Partida;
import com.sofkau.model.partida.gateways.PartidaRepository;
import com.sofkau.model.ronda.Ronda;
import com.sofkau.usecase.carta.mostrarcartas.MostrarCartasUseCase;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class CrearPartidaUseCase {

    private final PartidaRepository repository;
    private final  MostrarCartasUseCase mostrarCartasUseCase;

    public Mono<Partida> crearPartida(){

        List<Carta> cartas =new ArrayList<>();
        Ronda ronda = new Ronda();
        mostrarCartasUseCase
                .mostrarCartas()
                .subscribe(carta -> cartas.add(carta));

        Mazo mazo =  Mazo.getMazo(cartas);

        return repository.save(Partida.builder()
                .mazo(mazo)
                .ronda(ronda)
                .build());

        }
}
