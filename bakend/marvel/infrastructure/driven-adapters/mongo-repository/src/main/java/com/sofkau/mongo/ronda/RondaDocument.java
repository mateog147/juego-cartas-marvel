package com.sofkau.mongo.ronda;

import com.sofkau.model.carta.Carta;
import com.sofkau.model.jugador.Jugador;
import com.sofkau.model.mazo.Mazo;
import com.sofkau.model.ronda.Ronda;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.Map;

@Document
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RondaDocument {

    @Id
    private String id;
    private Map<Carta, String> apuestas;

}
