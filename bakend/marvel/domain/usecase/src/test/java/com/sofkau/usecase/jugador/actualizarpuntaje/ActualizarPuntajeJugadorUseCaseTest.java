package com.sofkau.usecase.jugador.actualizarpuntaje;

import com.sofkau.model.jugador.Jugador;
import com.sofkau.model.jugador.gateways.JugadorRepository;
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
class ActualizarPuntajeJugadorUseCaseTest {

    @InjectMocks
    ActualizarPuntajeJugadorUseCase useCase;

    @Mock
    JugadorRepository repository;

    @Test
    public void actualizar_puntaje_de_jugador_test(){

        Jugador jugador = new Jugador("1","uid1", "jugador1", 0, new ArrayList<>());
        Mono<Jugador> jugadorMono = Mono.just(jugador);

        when(repository.findById(jugador.id())).thenReturn(jugadorMono);
        when(repository.save(Mockito.any(Jugador.class))).thenReturn(Mono.just(jugador.toBuilder().puntaje(10).build()));

        Jugador jugadorActualizado = jugador;
        useCase.actualizarPuntajeDelJugador("1", 10)
                .flatMap(jugador1 -> {
                    jugadorActualizado.setPuntaje(jugador1.getPuntaje());
                    return Mono.just(jugador1);
                })
                .subscribe(i -> System.out.println(i));

        Assertions.assertEquals(10, jugadorActualizado.getPuntaje());

        StepVerifier.create(useCase.actualizarPuntajeDelJugador(jugador.id(), 10))
                .expectNextMatches(jugador1 -> jugador1.getPuntaje().equals(10))
                .expectComplete()
                .verify();
    }


}