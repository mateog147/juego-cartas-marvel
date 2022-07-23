package com.sofkau.model.ronda;
import com.sofkau.model.carta.Carta;
import com.sofkau.model.jugador.Jugador;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;
import java.util.stream.Collectors;

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
        if(this.apuestas.size()<=1){
            return "non";
        }
        return this.apuestas.stream()
                .max(Comparator.comparingInt(Apuesta::getCartaXp))
                .orElse(new Apuesta("non",null))
                .getJugadorId();
    }

    public List<Carta> entregarCartas(){
        if(this.apuestas.size()<=1){
            return new ArrayList<Carta>();
        }
        List<Carta> cartasEntregar =  this.apuestas.stream()
                .map(apuesta -> apuesta.getCarta())
                .collect(Collectors.toList());
        this.apuestas.clear();
        return cartasEntregar;
    }

}
