package com.sofkau.usecase.partida.encontrarpartida;

import com.sofkau.model.carta.Carta;
import com.sofkau.model.mazo.Mazo;
import com.sofkau.model.partida.Partida;
import com.sofkau.model.partida.gateways.PartidaRepository;
import com.sofkau.model.ronda.Ronda;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EncontrarPartidaUseCaseTest {

    @InjectMocks
    EncontrarPartidaUseCase useCase;

    @Mock
    PartidaRepository repository;

    @Test
    public void encontrar_partida_test(){
        Carta carta1 = new Carta("1", "Hulk", 100, "xxx");
        Carta carta2 = new Carta("2", "Spiderman", 50, "xxx");
        List<Carta> cartas = List.of(carta1, carta2);

        Ronda ronda = new Ronda();
        Mazo mazo =  Mazo.getMazo(cartas);
        Partida partida = Partida.builder()
                .id("partidaId")
                .mazo(mazo)
                .ronda(ronda)
                .build();

        when(repository.findById("partidaId")).thenReturn(Mono.just(partida));

        StepVerifier.create(useCase.encontrarPartida("partidaId"))
                .expectNext(partida)
                .expectComplete()
                .verify();
    }

}