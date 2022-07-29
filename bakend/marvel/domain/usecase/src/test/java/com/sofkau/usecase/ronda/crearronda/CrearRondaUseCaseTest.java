package com.sofkau.usecase.ronda.crearronda;

import com.sofkau.model.jugador.Jugador;
import com.sofkau.model.jugador.gateways.JugadorRepository;
import com.sofkau.model.ronda.Ronda;
import com.sofkau.model.ronda.gateways.RondaRepository;
import com.sofkau.usecase.jugador.crearjugador.CrearJugadorUseCase;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CrearRondaUseCaseTest {

    @InjectMocks
    CrearRondaUseCase useCase;

    @Mock
    RondaRepository repository;

    @Test
    public void crear_ronda_test(){

        Ronda ronda = new Ronda("1", new ArrayList<>(), 1, "", 60);
        when(repository.save(Mockito.any(Ronda.class))).thenReturn(Mono.just(ronda));

        Ronda rondaNueva = new Ronda();

        useCase.crearRonda(ronda)
                .flatMap(ronda1 -> {
                    rondaNueva.setId(ronda1.getId());
                    rondaNueva.setNumero(ronda1.getNumero());
                    return Mono.just(ronda1);
                })
                .subscribe(i -> System.out.println(i));

        Assertions.assertEquals("1", rondaNueva.getId());

        StepVerifier.create(useCase.crearRonda(ronda))
                .expectNextMatches(ronda1 -> ronda1.getId().equals("1"))
                .expectComplete()
                .verify();
    }

}