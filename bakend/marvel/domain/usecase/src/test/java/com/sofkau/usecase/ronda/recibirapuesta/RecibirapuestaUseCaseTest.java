package com.sofkau.usecase.ronda.recibirapuesta;

import com.sofkau.model.carta.Carta;
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
import org.springframework.context.ApplicationEventPublisher;
import org.w3c.dom.events.Event;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;
import reactor.test.StepVerifier;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.validateMockitoUsage;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RecibirapuestaUseCaseTest {

    @InjectMocks
    RecibirapuestaUseCase useCase;

    @Mock
    RondaRepository repository;

    @Test
    public void recibir_apuesta_test(){

        Carta carta = new Carta("cartaId", "Hulk", 100, "xxx");
        Apuesta apuesta = new Apuesta("jugadorId", carta);
        Ronda ronda = new Ronda("rondaId", new ArrayList<>(), 1, "", 60);

        when(repository.findById("rondaId")).thenReturn(Mono.just(ronda));
        when(repository.save(Mockito.any()))
                .thenReturn(Mono.just(ronda));

        // comprobar que la informacion arojada por la operación
        Ronda rondaActualizada = ronda;
        useCase.recibirApuesta("rondaId", apuesta)
                .flatMap(ronda1 -> {
                    rondaActualizada.setApuestas(ronda1.getApuestas());
                    return Mono.just(ronda1);
                })
                .subscribe(i-> System.out.println(i));

        var cartaApostada = rondaActualizada.getApuestas().stream().findFirst().get().getCarta();
        Assertions.assertEquals("Hulk", cartaApostada.getNombre());

        /*
        StepVerifier.create(useCase.recibirApuesta("rondaId", apuesta))
                .expectNextMatches(ronda1 -> ronda1
                        .getApuestas()
                        .get(0)
                        .getJugadorId()
                        .equals("jugadorId"))
                .expectComplete()
                .verify();

         */
    }

}