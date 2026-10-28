package com.sofkau.usecase.jugador.actualizarperfil;

import com.sofkau.model.jugador.Jugador;
import com.sofkau.model.jugador.gateways.JugadorRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.util.Set;

@RequiredArgsConstructor
public class ActualizarPerfilJugadorUseCase {

    private static final Set<String> AVATARES_PERMITIDOS = Set.of(
            "assets/character-default.png",
            "assets/character-0.png",
            "assets/character-1.png",
            "assets/character-2.png",
            "assets/character-3.png",
            "assets/character-4.png",
            "assets/character-5.png"
    );

    private final JugadorRepository jugadorRepository;

    public Mono<Jugador> actualizar(String id, String nombre, String avatar) {
        String nombreLimpio = nombre == null ? "" : nombre.trim();
        if (nombreLimpio.isEmpty() || nombreLimpio.length() > 24) {
            return Mono.error(new IllegalArgumentException("El nombre debe tener entre 1 y 24 caracteres"));
        }
        if (avatar == null || avatar.isBlank() || !AVATARES_PERMITIDOS.contains(avatar)) {
            return Mono.error(new IllegalArgumentException("Avatar no permitido"));
        }

        return jugadorRepository.findById(id)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Jugador no encontrado: " + id)))
                .flatMap(jugador -> jugadorRepository.save(jugador.toBuilder()
                        .nombre(nombreLimpio)
                        .avatar(avatar)
                        .build()));
    }
}
