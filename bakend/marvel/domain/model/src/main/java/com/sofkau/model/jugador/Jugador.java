package com.sofkau.model.jugador;
import com.sofkau.model.carta.Carta;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class Jugador {
    private String id;
    private String uid;
    private String nombre;
    private Integer puntaje;
    private List<Carta> cartas;


    public String uid() {return uid;}

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

    public Boolean eliminarCarta(Carta carta){

        return this.cartas.remove(carta);
    }
}
