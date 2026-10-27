package com.sofkau.api.realtime;

import com.sofkau.model.partida.Partida;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PartidaRealtimeEvent {

    public static final String BET_PLACED = "BET_PLACED";
    public static final String ROUND_RESOLVED = "ROUND_RESOLVED";
    public static final String STATE = "STATE";
    public static final String PARTIDA_CERRADA = "PARTIDA_CERRADA";
    public static final String PARTIDA_INICIADA = "PARTIDA_INICIADA";

    private String type;
    private String partidaId;
    private Partida partida;
}
