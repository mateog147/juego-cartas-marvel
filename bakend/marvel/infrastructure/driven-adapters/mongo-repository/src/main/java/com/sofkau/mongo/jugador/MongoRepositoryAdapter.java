package com.sofkau.mongo.jugador;

import com.sofkau.model.carta.Carta;
import com.sofkau.model.carta.gateways.CartaRepository;
import com.sofkau.model.jugador.Jugador;
import com.sofkau.model.jugador.gateways.JugadorRepository;
import com.sofkau.mongo.carta.CartaDocument;
import com.sofkau.mongo.helper.AdapterOperations;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Repository
public class MongoRepositoryAdapter extends AdapterOperations<Jugador, JugadorDocument, String, MongoDBRepository>
 implements JugadorRepository
{

    public MongoRepositoryAdapter(MongoDBRepository repository, ObjectMapper mapper) {
        /**
         *  Could be use mapper.mapBuilder if your domain model implement builder pattern
         *  super(repository, mapper, d -> mapper.mapBuilder(d,ObjectModel.ObjectModelBuilder.class).build());
         *  Or using mapper.map with the class of the object model
         */
        super(repository, mapper, d -> mapper.map(d, Jugador.class));
    }


    @Override
    public Flux<Carta> agregarCartasGanadas(String jugadorId, List<Carta> cartas) {
        return null;
    }

    @Override
    public Mono<Jugador> rendirseEnRonda(String jugadorId, Jugador jugador) {
        return null;
    }

    @Override
    public Mono<Jugador> eliminarcartaApostada(String jugadorId, Carta carta) {
        return null;
    }
}
