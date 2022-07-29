package com.sofkau.usecase.events;

import com.sofkau.model.ronda.Ronda;
import org.springframework.context.ApplicationEvent;

public class RondaTerminadaEvent extends ApplicationEvent {
    public RondaTerminadaEvent(Ronda ronda) {
        super(ronda);
        //System.out.println("ronda terminada event");
    }
}
