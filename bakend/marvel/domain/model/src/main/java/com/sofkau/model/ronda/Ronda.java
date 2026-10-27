package com.sofkau.model.ronda;

import com.sofkau.model.carta.Carta;
import com.sofkau.model.ronda.values.Apuesta;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class Ronda {

    private String id;

    @Builder.Default
    private List<Apuesta> apuestas = new ArrayList<>();

    @Builder.Default
    private Integer numero = 1;

    @Builder.Default
    private String ultimoGanador = "";

    /** Round duration in seconds (server + clients). */
    @Builder.Default
    private int isTimerOn = 30;

    /** Epoch millis when betting for this round closes. */
    private Long finRondaEn;

    public void agregarApuesta(Apuesta apuesta) {
        Objects.requireNonNull(apuesta);
        if (apuestas == null) {
            this.apuestas = new ArrayList<>();
        }
        this.apuestas.add(apuesta);
    }

    public String determinarGanador() {
        if (this.apuestas == null || this.apuestas.size() <= 1) {
            return "non";
        }
        return this.apuestas.stream()
                .max(Comparator.comparingInt(Apuesta::getCartaXp))
                .orElse(new Apuesta("non", null))
                .getJugadorId();
    }

    public List<Carta> entregarCartas() {
        if (this.apuestas == null || this.apuestas.size() <= 1) {
            return new ArrayList<>();
        }
        List<Carta> cartasEntregar = this.apuestas.stream()
                .map(Apuesta::getCarta)
                .collect(Collectors.toList());
        this.apuestas.clear();
        return cartasEntregar;
    }

    public void next() {
        this.numero++;
    }

    /** Starts / restarts the betting window from now. */
    public void iniciarTemporizador() {
        int segundos = this.isTimerOn > 0 ? this.isTimerOn : 30;
        this.isTimerOn = segundos;
        this.finRondaEn = System.currentTimeMillis() + (segundos * 1000L);
    }
}
