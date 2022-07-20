package com.sofkau.model.jugador;
import com.sofkau.model.carta.Carta;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder(toBuilder = true)
public class Jugador {
    private String id;
    private String nombre;
    private Integer puntaje;
    private List<Carta> cartas;


    public Jugador(String id, String nombre, List<Carta> cartas) {
        this.id = id;
        this.nombre = nombre;
        this.cartas = cartas;
    }

    public Integer puntaje() {
        return puntaje;
    }

    public List<Carta> cartas() {
        return cartas;
    }

    public String id() {
        return id;
    }

    public String nombre() {
        return nombre;
    }
}
