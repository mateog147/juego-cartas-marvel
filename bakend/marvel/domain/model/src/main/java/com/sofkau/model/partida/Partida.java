package com.sofkau.model.partida;

import com.sofkau.model.jugador.Jugador;
import com.sofkau.model.mazo.Mazo;
import com.sofkau.model.ronda.Ronda;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Objects;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class Partida {

    /** Legacy alias for EN_JUEGO (old documents). */
    @Deprecated
    public static final String ESTADO_ABIERTA = "ABIERTA";
    public static final String ESTADO_ESPERANDO = "ESPERANDO";
    public static final String ESTADO_EN_JUEGO = "EN_JUEGO";
    public static final String ESTADO_CERRADA = "CERRADA";

    private String id;
    private List<Jugador> jugadores;
    private Ronda ronda;
    private Mazo mazo;

    /** ESPERANDO | EN_JUEGO | CERRADA (ABIERTA legacy = EN_JUEGO) */
    @Builder.Default
    private String estado = ESTADO_ESPERANDO;

    /** Player who created the table. */
    private String hostJugadorId;

    private Long creadaEn;
    private Long cerradaEn;
    private String ganadorJugadorId;

    /** Not closed — waiting or in play (includes legacy ABIERTA / null). */
    public boolean estaAbierta() {
        return !ESTADO_CERRADA.equals(estado);
    }

    public boolean estaEsperando() {
        return ESTADO_ESPERANDO.equals(estado);
    }

    /** Ready for bets/timer. Legacy null/ABIERTA count as in play. */
    public boolean estaEnJuego() {
        return estado == null
                || ESTADO_EN_JUEGO.equals(estado)
                || ESTADO_ABIERTA.equals(estado);
    }

    public boolean permiteJugar() {
        return estaEnJuego();
    }

    public boolean esHost(String jugadorId) {
        return jugadorId != null && !jugadorId.isBlank() && jugadorId.equals(hostJugadorId);
    }

    public boolean esMiembro(String jugadorId) {
        if (jugadorId == null || jugadorId.isBlank()) {
            return false;
        }
        if (esHost(jugadorId)) {
            return true;
        }
        if (jugadores == null) {
            return false;
        }
        return jugadores.stream().anyMatch(jugador ->
                Objects.equals(jugadorId, jugador.getId())
                        || Objects.equals(jugadorId, jugador.getUid()));
    }

    public void marcarCerrada(String ganadorId) {
        this.estado = ESTADO_CERRADA;
        this.cerradaEn = System.currentTimeMillis();
        if (ganadorId != null && !ganadorId.isBlank()) {
            this.ganadorJugadorId = ganadorId;
        }
    }

    public void marcarEnJuego() {
        this.estado = ESTADO_EN_JUEGO;
    }
}
