package com.sofkau.usecase.jugador.consultarjugadores;


import com.sofkau.model.jugador.Jugador;
import com.sofkau.model.jugador.gateways.JugadorRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
//@SpringBootTest(classes = ConsultarjugadoresUseCaseTest.class)
class ConsultarjugadoresUseCaseTest {

    @InjectMocks
    ConsultarjugadoresUseCase useCase;

    @Mock
    JugadorRepository repository;

    @Test
    public void consultar_un_jugador(){

        //Arrange
        Jugador jugador1 = new Jugador("1","uid1", "jugador1", 0, new ArrayList<>());
        Jugador jugador2 = new Jugador("2","uid2", "jugador2", 0, new ArrayList<>());
        Flux<Jugador> jugadores = Flux.fromIterable(List.of(jugador1, jugador2));
        when(repository.findAll()).thenReturn(jugadores);

        StepVerifier.create(useCase.consultarJugadores())
                .expectNextMatches(jugador -> jugador.id().equals("1"))
                .expectNextMatches(jugador -> jugador.id().equals("2"))
                .expectComplete()
                .verify();
    }
}