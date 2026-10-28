package com.sofkau.api.realtime;

import com.sofkau.model.partida.Partida;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LobbyRealtimeEvent {

    public static final String PERFIL_UPDATED = "PERFIL_UPDATED";
    public static final String MESA_CREADA = "MESA_CREADA";
    public static final String MESA_CERRADA = "MESA_CERRADA";
    public static final String MESA_INICIADA = "MESA_INICIADA";

    private String type;
    private String jugadorId;
    private String nombre;
    private String avatar;
    private String partidaId;
    private String hostJugadorId;
    private List<String> jugadorIds;
    private Partida partida;
}
