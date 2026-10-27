package com.sofkau.usecase.ronda.crearronda;

import com.sofkau.model.ronda.Ronda;
import com.sofkau.model.ronda.gateways.RondaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.ArrayList;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CrearRondaUseCaseTest {

    @InjectMocks
    CrearRondaUseCase useCase;

    @Mock
    RondaRepository repository;

    @Test
    public void crear_ronda_test() {
        Ronda ronda = Ronda.builder()
                .id("1")
                .apuestas(new ArrayList<>())
                .numero(1)
                .ultimoGanador("")
                .isTimerOn(60)
                .build();

        when(repository.save(Mockito.any(Ronda.class)))
                .thenAnswer(invocation -> Mono.just(invocation.getArgument(0)));

        StepVerifier.create(useCase.crearRonda(ronda))
                .expectNextMatches(ronda1 ->
                        "1".equals(ronda1.getId())
                                && ronda1.getFinRondaEn() != null
                                && ronda1.getFinRondaEn() > System.currentTimeMillis())
                .expectComplete()
                .verify();
    }
}
