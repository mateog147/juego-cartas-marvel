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

    /*
    public void agregarApuesta(Carta carta, String jugadorId){
        Objects.requireNonNull(carta);
        Objects.requireNonNull(jugadorId);
        if(apuestas == null){ this.apuestas = new HashMap<>();};

        this.apuestas.put(jugadorId, carta);

        System.out.println("la apuesta de la esta ronda fue:" + this.apuestas);
    }

     */


    /*
    public String determinarGanador(){
        var cartaGanadora = this.apuestas.values()
                .stream()
                .max(Comparator.comparingInt(Carta::getXp));
        //return this.apuestas.get(cartaGanadora);
        return "";
    }

     */

}
