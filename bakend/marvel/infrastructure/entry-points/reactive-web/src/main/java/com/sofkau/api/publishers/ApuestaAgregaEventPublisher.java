package com.sofkau.api.publishers;

import com.sofkau.usecase.events.ApuestaAgregaEvent;
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
public class ApuestaAgregaEventPublisher implements ApplicationListener<ApuestaAgregaEvent>, Consumer<FluxSink<ApuestaAgregaEvent>> {

    private final Executor executor = Executors.newSingleThreadExecutor();
    private final BlockingQueue<ApuestaAgregaEvent> queue =
            new LinkedBlockingQueue<>();


    @Override
    public void accept(FluxSink<ApuestaAgregaEvent> apuestaAgregaEventFluxSink) {
        this.executor.execute(() -> {
            while (true)
                try {
                    ApuestaAgregaEvent event = queue.take();
                    apuestaAgregaEventFluxSink.next(event);
                }
                catch (InterruptedException e) {
                    ReflectionUtils.rethrowRuntimeException(e);
                }
        });
    }

    @Override
    public void onApplicationEvent(ApuestaAgregaEvent event) {
        this.queue.offer(event);
    }
}
