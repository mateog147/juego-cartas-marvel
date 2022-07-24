package com.sofkau.usecase.jugador.consultarjugadores;

import com.sofkau.model.jugador.Jugador;
import com.sofkau.model.jugador.gateways.JugadorRepository;
import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static reactor.core.publisher.Mono.when;

class ConsultarjugadoresUseCaseTest {

    @Autowired
    ConsultarjugadoresUseCase useCase;

    @Mock
    private JugadorRepository jugadorRepository;

    @Test
    public void consultar_un_jugador(){
        //List<Jugador> jugadores = Arrays.asList(new Jugador("1","uid","test1",10,null));
        Jugador jugador = new Jugador("1","uid","test1",10,null);
        when(jugadorRepository.findAll()).thenReturn(Flux.just(jugador));

        StepVerifier.create(useCase.consultarJugadores())
                .expectNextMatches(jugador1 -> jugador1.id().equals("1"))
                .verifyComplete();

    }
}