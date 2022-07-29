package com.sofkau.api.config;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sofkau.api.publishers.ApuestaAgregaEventPublisher;
import com.sofkau.api.publishers.RondaTerminadaEventPublisher;
import com.sofkau.usecase.events.ApuestaAgregaEvent;
import com.sofkau.usecase.events.RondaTerminadaEvent;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.HandlerMapping;
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.server.support.WebSocketHandlerAdapter;
import reactor.core.publisher.Flux;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

@Configuration
public class WebSocketConfiguration {

    private  final Logger logger = Logger.getLogger("logger");

    @Bean
    Executor executor() {
        return Executors.newSingleThreadExecutor();
    }


    @Bean
    HandlerMapping handlerMapping (@Qualifier("webSocketHandler") WebSocketHandler wsh, @Qualifier("rondaWebSocketHandler") WebSocketHandler wsh2) {
        Map endpoints = Map.of("/ws/apuestas", wsh,"/ws/ganadores",wsh2);
        return new SimpleUrlHandlerMapping() {
            {
                setUrlMap(endpoints);
                setOrder(10);
            }
        };
    }


    @Bean
    WebSocketHandlerAdapter webSocketHandlerAdapter() {
        return new WebSocketHandlerAdapter();
    }

    @Bean
    WebSocketHandler webSocketHandler(ObjectMapper objectMapper, ApuestaAgregaEventPublisher eventPublisher) {

        Flux<ApuestaAgregaEvent> publish = Flux
                .create(eventPublisher)
                .share();

        return session -> {
            Flux<WebSocketMessage> messageFlux = publish
                    .map(evt -> {
                        try {

                            return objectMapper.writeValueAsString(evt.getSource());
                        }
                        catch (JsonProcessingException e) {
                            throw new RuntimeException(e);
                        }
                    })
                    .map(str -> {
                        logger.info("sending " + str);
                        return session.textMessage(str);
                    });

            return session.send(messageFlux);
        };
    }

    @Bean
    WebSocketHandler rondaWebSocketHandler(ObjectMapper objectMapper, RondaTerminadaEventPublisher rondaEventPublisher) {

        Flux<RondaTerminadaEvent> publishR = Flux
                .create(rondaEventPublisher)
                .share();

        return session -> {
            Flux<WebSocketMessage> messageFlux = publishR
                    .map(evt -> {
                        try {

                            return objectMapper.writeValueAsString(evt.getSource());
                        }
                        catch (JsonProcessingException e) {
                            throw new RuntimeException(e);
                        }
                    })
                    .map(str -> {
                        logger.info("sending " + str);
                        return session.textMessage(str);
                    });

            return session.send(messageFlux);
        };
    }

}
