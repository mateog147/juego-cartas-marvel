package com.sofkau.model.mazo;
import com.sofkau.model.carta.Carta;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;

@Data
public class Mazo {

    private static final Mazo instanciaMazo = new Mazo();
    private List<Carta> mazo;

    public Mazo() {
        this.mazo = new ArrayList<>();
    }

    public static Mazo getMazo(List<Carta> mazo){
        instanciaMazo.setMazo(mazo);
        return instanciaMazo;
    }


    public List<Carta> mazo() {
        return mazo;
    }


    public List<Carta> asignarCartas(){

        int cantidadCartasARepartir = 5;
        List<Carta> cartas = new ArrayList<>();

        for (int i = 0; i < cantidadCartasARepartir; i++) {
            var carta = seleccionarCarta();
            cartas.add(carta);
            mazo.remove(carta);
        }

        return cartas;
    }

    private Carta seleccionarCarta(){
        return mazo.stream().findAny().get();
    }

    public void barajar() {
        Collections.shuffle(this.mazo);
    }
}
