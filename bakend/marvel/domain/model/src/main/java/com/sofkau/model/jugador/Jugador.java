package com.sofkau.model.jugador;
import com.sofkau.model.carta.Carta;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class Jugador {
    private String id;
    private String nombre;
    private Integer puntaje;
    private List<Carta> cartas;




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

    public List<Carta> agregarCartas(List<Carta> cartasNueva){
        List<Carta> lista = this.cartas;
        lista.addAll(cartasNueva);
        return lista;
    }
}
