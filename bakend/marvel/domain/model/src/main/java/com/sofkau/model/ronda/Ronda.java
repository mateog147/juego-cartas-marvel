package com.sofkau.model.ronda;
import com.sofkau.model.carta.Carta;
import com.sofkau.model.jugador.Jugador;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class Ronda {

    private String id;
    private Map<String, Carta> apuestas;



    public Map<String, Carta> agregarApuesta(Carta carta, String jugadorId){
        Objects.requireNonNull(carta);
        Objects.requireNonNull(jugadorId);
        if(apuestas == null){ this.apuestas = new HashMap<>();};

        this.apuestas.put(jugadorId, carta);

        System.out.println("la apuesta de la esta ronda fue:" + this.apuestas);
        return this.apuestas;
    }

    public String determinarGanador(){
        var cartaGanadora = this.apuestas.values()
                .stream()
                .max(Comparator.comparingInt(Carta::getXp));
        //return this.apuestas.get(cartaGanadora);
        return "";
    }

}
