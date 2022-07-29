package com.sofkau.usecase.jugador.crearjugador;

import com.sofkau.model.jugador.Jugador;
import com.sofkau.model.jugador.gateways.JugadorRepository;
import com.sofkau.usecase.jugador.actualizarpuntaje.ActualizarPuntajeJugadorUseCase;
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
class CrearJugadorUseCaseTest {

    @InjectMocks
    CrearJugadorUseCase useCase;

    @Mock
    JugadorRepository repository;

    @Test
    public void crear_jugador_test(){

        Jugador jugador = new Jugador("1","uid1", "jugador1", 0, new ArrayList<>());
        when(repository.findByUid(jugador.uid())).thenReturn(Mono.just(jugador));
        when(repository.save(Mockito.any(Jugador.class))).thenReturn(Mono.just(jugador));

        // comprobar que la informacion arojada por la operación
        Jugador jugadorNuevo = new Jugador();
        useCase.crearNuevoJugador(jugador)
                .flatMap(jugador1 -> {
                    jugadorNuevo.setId(jugador1.getId());
                    jugadorNuevo.setUid(jugador1.getUid());
                    return Mono.just(jugador1);
                })
                .subscribe(i -> System.out.println(i));

        Assertions.assertEquals("uid1", jugadorNuevo.uid());

        StepVerifier.create(useCase.crearNuevoJugador(jugador))
                .expectNextMatches(jugador1 -> jugador1.id().equals("1"))
                .expectComplete()
                .verify();
    }

}