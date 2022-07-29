package com.sofkau.usecase.ronda.recibirapuesta;

import com.sofkau.model.carta.Carta;
import com.sofkau.model.jugador.gateways.JugadorRepository;
import com.sofkau.model.ronda.Ronda;
import com.sofkau.model.ronda.gateways.RondaRepository;
import com.sofkau.model.ronda.values.Apuesta;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RecibirapuestaUseCaseTest {

    @InjectMocks
    RecibirapuestaUseCase useCase;

    @Mock
    RondaRepository repository;

    @Test
    public void recibir_apuesta_test(){

        //Jugador jugador = new Jugador("jugadorId","uid1", "jugador1", 0, new ArrayList<>());
        Carta carta = new Carta("cartaId", "Hulk", 100, "XXX");
        Apuesta apuesta = new Apuesta( "jugadorId", carta);
        Ronda ronda = new Ronda("rondaId", new ArrayList<>(), 1, "", 60);

        when( repository.findById(ronda.getId())).thenReturn(Mono.just(ronda));
        when(repository.save(Mockito.any(Ronda.class)))
                .thenReturn(Mono.just(ronda.toBuilder().apuestas(List.of(apuesta)).build()));

        // comprobar que la informacion arojada por la operación
        Ronda rondaActualizada = ronda;
        useCase.recibirApuesta("jugadorId", apuesta )
                .flatMap(apuesta1 -> {
                    rondaActualizada.setApuestas(apuesta1.getApuestas());
                    return Mono.just(apuesta1);
                })
                .subscribe(i -> System.out.println(i));

        var cartaApostada = rondaActualizada.getApuestas().stream().findFirst().get().getCarta();
        Assertions.assertEquals("Hulk", cartaApostada.getNombre());

        StepVerifier.create(useCase.recibirApuesta("jugadorId", apuesta))
                .expectNextMatches(ronda1 -> ronda1.getApuestas()
                        .stream()
                        .findFirst()
                        .get()
                        .getCarta()
                        .getNombre().equals("Hulk"))
                .expectComplete()
                .verify();
    }

}