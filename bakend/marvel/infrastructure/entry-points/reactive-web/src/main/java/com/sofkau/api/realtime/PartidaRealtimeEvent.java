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

    private String type;
    private String partidaId;
    private Partida partida;
}
