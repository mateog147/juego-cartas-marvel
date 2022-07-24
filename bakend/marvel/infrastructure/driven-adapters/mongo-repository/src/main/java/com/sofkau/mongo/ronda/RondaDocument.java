package com.sofkau.mongo.ronda;

import com.sofkau.model.ronda.values.Apuesta;
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
public class RondaDocument {

    @Id
    private String id;
    private List<Apuesta> apuestas;

}
