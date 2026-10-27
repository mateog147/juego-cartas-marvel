package com.sofkau.mongo.partida;

import com.sofkau.model.jugador.Jugador;
import com.sofkau.model.mazo.Mazo;
import com.sofkau.model.ronda.Ronda;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PartidaDocument {

    @Id
    private String id;
    private List<Jugador> jugadores;
    private Ronda ronda;
    private Mazo mazo;
    private String estado;
    private String hostJugadorId;
    private Long creadaEn;
    private Long cerradaEn;
    private String ganadorJugadorId;
}
