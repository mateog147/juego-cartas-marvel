package com.sofkau.usecase.jugador.consultarjugadores;


import com.sofkau.model.jugador.Jugador;
import com.sofkau.model.jugador.gateways.JugadorRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
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
        Jugador jugador = Jugador.of("1","uid2", "jugador1");
        Flux<Jugador> jugadores = Flux.fromIterable(List.of(jugador));
        when(repository.findAll()).thenReturn(jugadores);

        // act
        List<Jugador> prueba = new ArrayList<>();
        var event = useCase.consultarJugadores().subscribe(jugador1 -> prueba.add(jugador1));

        Assertions.assertEquals(prueba.get(0).getId(), "1");



        /*
        var jugadorUsecase = new ArrayList<>();
        var events = useCase.consultarJugadores()
                .subscribe(jugador1 -> jugadorUsecase.add(jugador1));


        UseCaseHandler

        publisher.subscribe(subscriber);
        useCase.setRequest(values);
        useCase.setIdentify(identifyExecutor());
        useCase.setUseCaseCallback((UseCase.UseCaseFormat<R>) publisher);
        useCase.run();

        List<Jugador> jugadores = Arrays.asList(Jugador.builder().id("1").build());

        when(repository.findAll()).thenReturn(Flux.fromIterable(jugadores));

        StepVerifier.create(useCase.consultarJugadores())
                .expectNextMatches(jugador -> jugador.id().equals("1"))
                .thenCancel()
                .verify();

        useCase.M

         */
    }
}