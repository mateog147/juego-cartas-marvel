package com.sofkau.usecase.jugador.consultarjugadores;

import com.sofkau.model.carta.Carta;
import com.sofkau.model.jugador.Jugador;
import com.sofkau.model.jugador.gateways.JugadorRepository;
import org.assertj.core.util.Arrays;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static reactor.core.publisher.Mono.when;

@ExtendWith(MockitoExtension.class)
class ConsultarjugadoresUseCaseTest {

    @MockBean
    JugadorRepository repository;

    @InjectMocks
    ConsultarjugadoresUseCase useCase;

    @Autowired
    private WebTestClient webTestClient;

    @Test
    public void consultarTodosLosJugadores(){

        Jugador jug = new Jugador("id1", "uid1", "Jugador 1", 0, new ArrayList<>());
        Flux<Jugador> jugadorModel = Flux.fromIterable(List.of(jug));

        webTestClient.get()
                        .uri("/api/jugador/")
                                .exchange()
                .expectStatus().isOk()
                .expectBody(Jugador.class)
                .consumeWith(personEntityExchangeResult -> {
                    var person = personEntityExchangeResult.getResponseBody();
                    assert person != null;
                });



        when(repository.findAll()).thenReturn(jugadorModel);

        StepVerifier.create(useCase.consultarJugadores())
                .expectNextMatches(jugador -> jugador.equals("id1"))
                .expectComplete()
                .verify();

    }

    private Flux<Jugador> history() {
        List<Carta> cartas = new ArrayList<Carta>();
        Jugador jugador1 = new Jugador("id1", "uid1", "Jugador 1", 0, cartas);
        Jugador jugador2 = new Jugador("id2", "uid2", "Jugador 2", 0, cartas);

        return Flux.fromIterable(List.of(
                jugador1
        ));
    }

    /*
    @Autowired
    ConsultarjugadoresUseCase useCase;

    @Mock
    private final JugadorRepository jugadorRepository;

    @BeforeAll
    public void setUp(){
        MockitoAnnotations.openMocks(this);
    }
    @Test
    public void consultar_un_jugador(){
        //List<Jugador> jugadores = Arrays.asList(new Jugador("1","uid","test1",10,null));
        Jugador jugador = new Jugador("1","uid","test1",10,null);
        when(jugadorRepository.findAll()).thenReturn(Flux.just(jugador));

        StepVerifier.create(useCase.consultarJugadores())
                .expectNextMatches(jugador1 -> jugador1.id().equals("1"))
                .verifyComplete();

    }*/
}