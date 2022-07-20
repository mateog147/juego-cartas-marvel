package com.sofkau.model.carta;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class Carta {

    private String id;
    private String nombre;
    private Integer xp;
    private String imagen;


    public String id() {
        return id;
    }

    public Integer xp() {
        return xp;
    }

    public String imagen() {
        return imagen;
    }

    public String nombre() {
        return nombre;
    }
}
