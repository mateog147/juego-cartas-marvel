package com.sofkau.mongo.carta;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartaDocument {

    @Id
    private String id;
    private String nombre;
    private Integer xp;
    private String imagen;

}
