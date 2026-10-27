package com.sofkau.usecase.partida.cerrarpartida;

import com.sofkau.model.partida.Partida;
import com.sofkau.model.partida.gateways.PartidaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CerrarPartidaUseCaseTest {

    @InjectMocks
    CerrarPartidaUseCase useCase;

    @Mock
    PartidaRepository partidaRepository;

    @Test
    void cierraPartidaAbierta() {
        Partida abierta = Partida.builder()
                .id("p1")
                .estado(Partida.ESTADO_EN_JUEGO)
                .build();

        when(partidaRepository.findById("p1")).thenReturn(Mono.just(abierta));
        when(partidaRepository.save(any(Partida.class)))
                .thenAnswer(invocation -> Mono.just(invocation.getArgument(0)));

        StepVerifier.create(useCase.cerrar("p1", "j1"))
                .assertNext(partida -> {
                    assertEquals(Partida.ESTADO_CERRADA, partida.getEstado());
                    assertEquals("j1", partida.getGanadorJugadorId());
                    assertNotNull(partida.getCerradaEn());
                    assertFalse(partida.estaAbierta());
                })
                .verifyComplete();

        ArgumentCaptor<Partida> captor = ArgumentCaptor.forClass(Partida.class);
        verify(partidaRepository).save(captor.capture());
        assertEquals(Partida.ESTADO_CERRADA, captor.getValue().getEstado());
    }

    @Test
    void noVuelveACerrarSiYaEstaCerrada() {
        Partida cerrada = Partida.builder()
                .id("p1")
                .estado(Partida.ESTADO_CERRADA)
                .cerradaEn(123L)
                .build();

        when(partidaRepository.findById("p1")).thenReturn(Mono.just(cerrada));

        StepVerifier.create(useCase.cerrar("p1"))
                .expectNext(cerrada)
                .verifyComplete();
    }
}
