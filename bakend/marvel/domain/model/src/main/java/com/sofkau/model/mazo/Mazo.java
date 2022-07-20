package com.sofkau.model.mazo;
import com.sofkau.model.carta.Carta;
import lombok.Builder;
import lombok.Data;

import java.util.*;

@Data
@Builder(toBuilder = true)
public class Mazo {

    private String id;
    private Set<Carta> mazo;

    public Mazo(String id, Set<Carta> mazo) {
        this.id = id;
        this.mazo = barajarMazo(mazo);
    }

    private Set<Carta> barajarMazo(Set<Carta> mazo) {
        Collections.shuffle(Arrays.asList(mazo.toArray()));
        Set<Carta> mazo_barajado = new HashSet<>();
        mazo_barajado.addAll(mazo);
        return mazo_barajado;
    }

    public Set<Carta> mazo() {
        return mazo;
    }

    public String id() {
        return id;
    }

    public Set<Carta> asignarCartas(){

        int cantidadCartasARepartir = 5;
        Set<Carta> cartas = new TreeSet<Carta>();

        for (int i = 0; i < cantidadCartasARepartir-1; i++) {
            var carta = seleccionarCarta();
            cartas.add(carta);
            mazo.remove(carta);
        }

        return cartas;
    }

    private Carta seleccionarCarta(){
        return mazo.stream().findAny().get();
    }

}
