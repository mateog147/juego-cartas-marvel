package com.sofkau.usecase.partida.listarpartidasabiertas;

import com.sofkau.model.jugador.Jugador;
import com.sofkau.model.partida.Partida;
import com.sofkau.model.partida.gateways.PartidaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.List;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ListarPartidasAbiertasUseCaseTest {

    @InjectMocks
    ListarPartidasAbiertasUseCase useCase;

    @Mock
    PartidaRepository partidaRepository;

    @Test
    void listaSoloAbiertasDelJugador() {
        Partida mia = Partida.builder()
                .id("a")
                .estado(Partida.ESTADO_ESPERANDO)
                .hostJugadorId("j1")
                .build();
        Partida comoJugador = Partida.builder()
                .id("b")
                .estado(Partida.ESTADO_EN_JUEGO)
                .jugadores(List.of(Jugador.builder().id("j1").nombre("Yo").build()))
                .build();
        Partida porUid = Partida.builder()
                .id("e")
                .estado(Partida.ESTADO_ESPERANDO)
                .jugadores(List.of(Jugador.builder().uid("uid-j1").nombre("Yo uid").build()))
                .build();
        Partida deOtro = Partida.builder()
                .id("c")
                .estado(Partida.ESTADO_EN_JUEGO)
                .hostJugadorId("j2")
                .jugadores(List.of(Jugador.builder().id("j2").nombre("Otro").build()))
                .build();
        Partida cerrada = Partida.builder()
                .id("d")
                .estado(Partida.ESTADO_CERRADA)
                .hostJugadorId("j1")
                .build();
        Partida legacySinEstado = Partida.builder()
                .id("f")
                .jugadores(List.of(Jugador.builder().id("j1").nombre("Legacy").build()))
                .build();

        when(partidaRepository.findAll())
                .thenReturn(Flux.just(mia, comoJugador, porUid, deOtro, cerrada, legacySinEstado));

        StepVerifier.create(useCase.listar("j1"))
                .expectNext(mia)
                .expectNext(comoJugador)
                .expectNext(legacySinEstado)
                .verifyComplete();

        StepVerifier.create(useCase.listar("uid-j1"))
                .expectNext(porUid)
                .verifyComplete();
    }
}
