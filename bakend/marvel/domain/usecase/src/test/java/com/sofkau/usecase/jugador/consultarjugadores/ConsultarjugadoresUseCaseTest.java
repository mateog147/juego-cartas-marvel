package com.sofkau.usecase.jugador.consultarjugadores;



import com.sofkau.model.jugador.Jugador;
import com.sofkau.model.jugador.gateways.JugadorRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)

@SpringBootTest(classes = ConsultarjugadoresUseCaseTest.class)
class ConsultarjugadoresUseCaseTest {

    @Autowired
    ConsultarjugadoresUseCase useCase;

    @Mock
    JugadorRepository repository;

    @Test
    public void consultar_un_jugador(){
        List<Jugador> jugadores = Arrays.asList(Jugador.builder().id("1").build());

        when(repository.findAll()).thenReturn(Flux.fromIterable(jugadores));

        StepVerifier.create(useCase.consultarJugadores())
                .expectNextMatches(jugador -> jugador.id().equals("1"))
                .thenCancel()
                .verify();

    }
}