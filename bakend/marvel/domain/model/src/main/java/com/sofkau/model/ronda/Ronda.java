package com.sofkau.model.ronda;
import com.sofkau.model.carta.Carta;
import com.sofkau.model.jugador.Jugador;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Comparator;
import java.util.Map;
import java.util.Objects;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class Ronda {

    private String id;
    private Map<Carta, String> apuestas;

    public void agregarApuesta(Carta carta, String jugadorId){
        Objects.requireNonNull(carta);
        Objects.requireNonNull(jugadorId);

        this.apuestas.put(carta, jugadorId);
    }

    public String determinarGanador(){
        var cartaGanadora = this.apuestas.keySet()
                .stream()
                .max(Comparator.comparingInt(Carta::getXp));
        return this.apuestas.get(cartaGanadora);
    }

}
