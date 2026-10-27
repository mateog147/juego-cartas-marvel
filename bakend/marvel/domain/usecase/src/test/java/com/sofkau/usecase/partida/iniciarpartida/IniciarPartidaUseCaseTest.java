package com.sofkau.usecase.partida.iniciarpartida;

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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class IniciarPartidaUseCaseTest {

    @InjectMocks
    IniciarPartidaUseCase useCase;

    @Mock
    PartidaRepository partidaRepository;

    @Test
    void hostIniciaPartidaEnEspera() {
        Partida esperando = Partida.builder()
                .id("p1")
                .estado(Partida.ESTADO_ESPERANDO)
                .hostJugadorId("host1")
                .ronda(Ronda.builder().numero(1).isTimerOn(30).build())
                .build();

        when(partidaRepository.findById("p1")).thenReturn(Mono.just(esperando));
        when(partidaRepository.save(any(Partida.class)))
                .thenAnswer(inv -> Mono.just(inv.getArgument(0)));

        StepVerifier.create(useCase.iniciar("p1", "host1"))
                .assertNext(partida -> {
                    assertEquals(Partida.ESTADO_EN_JUEGO, partida.getEstado());
                    assertNotNull(partida.getRonda().getFinRondaEn());
                })
                .verifyComplete();
    }

    @Test
    void noHostNoPuedeIniciar() {
        Partida esperando = Partida.builder()
                .id("p1")
                .estado(Partida.ESTADO_ESPERANDO)
                .hostJugadorId("host1")
                .ronda(new Ronda())
                .build();

        when(partidaRepository.findById("p1")).thenReturn(Mono.just(esperando));

        StepVerifier.create(useCase.iniciar("p1", "otro"))
                .expectErrorMatches(err -> err instanceof IllegalStateException
                        && err.getMessage().contains("host"))
                .verify();
    }

    @Test
    void yaEnJuegoDevuelveMisma() {
        Partida enJuego = Partida.builder()
                .id("p1")
                .estado(Partida.ESTADO_EN_JUEGO)
                .hostJugadorId("host1")
                .ronda(Ronda.builder().finRondaEn(999L).build())
                .build();

        when(partidaRepository.findById("p1")).thenReturn(Mono.just(enJuego));

        StepVerifier.create(useCase.iniciar("p1", "host1"))
                .expectNext(enJuego)
                .verifyComplete();
    }
}
