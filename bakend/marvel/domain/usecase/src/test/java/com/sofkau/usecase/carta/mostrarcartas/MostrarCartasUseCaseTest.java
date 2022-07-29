package com.sofkau.usecase.carta.mostrarcartas;

import com.sofkau.model.carta.Carta;
import com.sofkau.model.carta.gateways.CartaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MostrarCartasUseCaseTest {

    @InjectMocks
    MostrarCartasUseCase useCase;

    @Mock
    CartaRepository repository;

    @Test
    public void mostrar_cartas_test() {

        //Arrange
        Carta carta1 = new Carta("1", "Hulk", 100, "xxx");
        Carta carta2 = new Carta("2", "Spiderman", 50, "xxx");

        Flux<Carta> cartas = Flux.fromIterable(List.of(carta1, carta2));
        when(repository.findAll()).thenReturn(cartas);

        StepVerifier.create(useCase.mostrarCartas())
                .expectNextMatches(carta -> carta.nombre().equals("Hulk"))
                .expectNextMatches(carta -> carta.nombre().equals("Spiderman"))
                .expectComplete()
                .verify();

    }

}