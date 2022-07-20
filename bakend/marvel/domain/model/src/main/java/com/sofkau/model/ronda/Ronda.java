package com.sofkau.model.ronda;
import com.sofkau.model.carta.Carta;
import com.sofkau.model.jugador.Jugador;
import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder(toBuilder = true)
public class Ronda {

    private String id;
    Map<Carta, String> apuestas;

    /*public void agregarApuesta(Carta carta, Jugador jugador){
        this.apuestas.put(carta, jugador.id());
    }*/

}
