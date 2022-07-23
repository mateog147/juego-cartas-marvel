package com.sofkau.model.ronda;

import com.sofkau.model.carta.Carta;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class Apuesta {

    private String jugadorId;
    private Carta carta;

    public Integer getCartaXp(){
        return this.carta.getXp();
    }

}
