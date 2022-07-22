package com.sofkau.mongo.jugador;

import com.sofkau.model.carta.Carta;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
@Document
@Data
@AllArgsConstructor
@NoArgsConstructor
public class JugadorDocument {

    @Id
    private String id;
    private String uid;
    private String nombre;
    private Integer puntaje;
    private List<Carta> cartas;
}
