package com.sofkau.usecase.ronda.ganadorronda;

import com.sofkau.model.ronda.Ronda;
import com.sofkau.model.ronda.gateways.RondaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.ArrayList;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GanadorRondaUseCaseTest {

    @InjectMocks
    GanadorRondaUseCase useCase;

    @Mock
    RondaRepository repository;

    @Mock
    ApplicationEventPublisher publisher;

    @Test
    public void terminar_ronda_test() {
        Ronda ronda = new Ronda("1", new ArrayList<>(), 1, "", 60);
        when(repository.save(Mockito.any(Ronda.class)))
                .thenAnswer(invocation -> {
                    Ronda saved = invocation.getArgument(0);
                    return Mono.just(saved);
                });

        StepVerifier.create(useCase.terminarRonda(ronda, "jugadorGanador"))
                .expectNextMatches(ronda1 ->
                        "jugadorGanador".equals(ronda1.getUltimoGanador())
                                && ronda1.getNumero() == 2)
                .expectComplete()
                .verify();
    }
}
