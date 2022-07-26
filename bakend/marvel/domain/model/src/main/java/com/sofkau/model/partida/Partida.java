package com.sofkau.model.partida;
import com.sofkau.model.jugador.Jugador;
import com.sofkau.model.mazo.Mazo;
import com.sofkau.model.ronda.Ronda;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class Partida {
    //TODO trasladar logica basica de casos de uso a entidad para mejorar mantenibilidad
    private String id;
    private  List<Jugador> jugadores;
    private  Ronda ronda;
    private  Mazo mazo;

}
