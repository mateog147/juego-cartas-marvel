package com.sofkau.usecase.events;

import com.sofkau.model.ronda.Ronda;
import org.springframework.context.ApplicationEvent;

public class ApuestaAgregaEvent extends ApplicationEvent {
    public ApuestaAgregaEvent(Ronda ronda) {
        super(ronda);
        System.out.println("nuevo evento apuesta");
    }
}
