package com.sofkau.api.publishers;

import com.sofkau.usecase.events.RondaTerminadaEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;
import reactor.core.publisher.FluxSink;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Consumer;

@Component
public class RondaTerminadaEventPublisher implements ApplicationListener<RondaTerminadaEvent>, Consumer<FluxSink<RondaTerminadaEvent>> {

    private final Executor executor = Executors.newSingleThreadExecutor();
    private final BlockingQueue<RondaTerminadaEvent> queue =
            new LinkedBlockingQueue<>();


    @Override
    public void accept(FluxSink<RondaTerminadaEvent> rondaTerminadaEventFluxSink) {
        this.executor.execute(() -> {
            while (true)
                try {
                    RondaTerminadaEvent event = queue.take();
                    System.out.println("Ejecutando partida terminada");
                    rondaTerminadaEventFluxSink.next(event);
                }
                catch (InterruptedException e) {
                    ReflectionUtils.rethrowRuntimeException(e);
                }
        });
    }

    @Override
    public void onApplicationEvent(RondaTerminadaEvent event) {
        this.queue.offer(event);
        System.out.println("Terminada llega a publisher");
    }
}
