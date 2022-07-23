package com.sofkau.model.ronda;
import com.sofkau.model.carta.Carta;
import com.sofkau.model.jugador.Jugador;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class Ronda {

    private String id;
    @Builder.Default
    private List<Apuesta> apuestas = new ArrayList<>();

    public void agregarApuesta ( Apuesta apuesta ) {
        Objects.requireNonNull(apuesta);
        if(apuestas == null){this.apuestas = new ArrayList<>(); };
        this.apuestas.add(apuesta);
        //return this.apuestas;
    }


    public String determinarGanador(){
        var cartaGanadora = this.apuestas.stream()
                //.reduce((x, y) -> x.getCarta().getXp().compareTo(y.getCarta().getXp()) <= 0  ? x : y).get();
                .max(Comparator.comparingInt(apuesta -> apuesta.getCarta().getXp()));
        return this.apuestas.get(apuestas.indexOf(cartaGanadora)).getJugadorId();

    }

}
