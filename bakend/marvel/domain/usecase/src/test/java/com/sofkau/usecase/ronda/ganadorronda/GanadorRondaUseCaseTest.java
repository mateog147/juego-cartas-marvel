package com.sofkau.usecase.ronda.ganadorronda;

import com.sofkau.model.ronda.Ronda;
import com.sofkau.model.ronda.gateways.RondaRepository;
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

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GanadorRondaUseCaseTest {

    @InjectMocks
    GanadorRondaUseCase useCase;

    @Mock
    RondaRepository repository;

    @Test
    public void terminar_ronda_test(){

        Ronda ronda = new Ronda("1", new ArrayList<>(), 1, "", 60);
        when(repository.save(Mockito.any(Ronda.class)))
                .thenReturn(Mono.just(ronda.toBuilder().numero(2).ultimoGanador("jugadorGanador").build()));

        // comprobamos la operacion te temrinar partida
        Ronda rondaNueva = new Ronda();
        useCase.terminarRonda(ronda, "jugadorGanador")
                .flatMap(ronda1 -> {
                    rondaNueva.setNumero(ronda1.getNumero());
                    rondaNueva.setUltimoGanador("jugadorGanador");
                    return Mono.just(ronda1);
                })
                .subscribe(i -> System.out.println(i));

        Assertions.assertEquals("jugadorGanador", rondaNueva.getUltimoGanador());
        Assertions.assertEquals(2, rondaNueva.getNumero());

        // comprobamos la operacion save
        StepVerifier.create(useCase.terminarRonda(ronda, "jugadorGanador"))
                .expectNextMatches(ronda1 -> ronda1.getUltimoGanador().equals("jugadorGanador"))
                .expectComplete()
                .verify();
    }


}