package com.sofkau.usecase.jugador.actualizarperfil;

import com.sofkau.model.jugador.Jugador;
import com.sofkau.model.jugador.gateways.JugadorRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ActualizarPerfilJugadorUseCaseTest {

    @InjectMocks
    ActualizarPerfilJugadorUseCase useCase;

    @Mock
    JugadorRepository repository;

    @Test
    void actualizaNombreYAvatar() {
        Jugador actual = Jugador.builder()
                .id("1")
                .uid("uid1")
                .nombre("Viejo")
                .puntaje(10)
                .cartas(new ArrayList<>())
                .avatar("assets/character-default.png")
                .build();

        when(repository.findById("1")).thenReturn(Mono.just(actual));
        when(repository.save(any(Jugador.class)))
                .thenAnswer(inv -> Mono.just(inv.getArgument(0)));

        StepVerifier.create(useCase.actualizar("1", "Nuevo Heroe", "assets/character-2.png"))
                .assertNext(j -> {
                    assertEquals("Nuevo Heroe", j.getNombre());
                    assertEquals("assets/character-2.png", j.getAvatar());
                    assertEquals(10, j.getPuntaje());
                })
                .verifyComplete();
    }

    @Test
    void rechazaAvatarInvalido() {
        StepVerifier.create(useCase.actualizar("1", "Ok", "https://evil.example/x.png"))
                .expectError(IllegalArgumentException.class)
                .verify();
    }

    @Test
    void rechazaNombreVacio() {
        StepVerifier.create(useCase.actualizar("1", "  ", "assets/character-0.png"))
                .expectError(IllegalArgumentException.class)
                .verify();
    }
}
