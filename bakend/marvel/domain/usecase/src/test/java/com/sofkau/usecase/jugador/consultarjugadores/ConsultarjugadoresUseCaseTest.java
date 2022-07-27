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
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(MockitoExtension.class)

@SpringBootTest(classes = ConsultarjugadoresUseCaseTest.class)
class ConsultarjugadoresUseCaseTest {

    static class Invento implements JugadorRepository{

        @Override
        public Mono<Jugador> save(Jugador jugador) {
            return null;
        }

        @Override
        public Flux<Jugador> findAll() {
            return Flux.just(Jugador.builder().id("1").build());
        }

        @Override
        public Mono<Jugador> rendirseEnRonda(String jugadorId, Jugador jugador) {
            return null;
        }

        @Override
        public Mono<Jugador> findById(String id) {
            return null;
        }

        @Override
        public Flux<Jugador> findAllById(Iterable<String> ids) {
            return null;
        }

        @Override
        public Mono<Jugador> findByUid(String uid) {
            return null;
        }
    }

    JugadorRepository repository = new Invento();

    @Autowired
    ConsultarjugadoresUseCase useCase;

    @Test
    public void consultar_un_jugador(){
        //List<Jugador> jugadores = Arrays.asList(Jugador.builder().id());

        //when(repository.findAll()).thenReturn(Flux.just(Jugador.builder().id("1").build()));


        StepVerifier.create(useCase.consultarJugadores())
                .expectNextMatches(jugador -> jugador.id() == "1")
                .thenCancel()
                .verify();

    }
}