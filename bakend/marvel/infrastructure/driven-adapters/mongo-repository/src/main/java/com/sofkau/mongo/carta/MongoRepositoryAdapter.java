package com.sofkau.mongo.carta;

import com.sofkau.model.carta.Carta;
import com.sofkau.model.carta.gateways.CartaRepository;
import com.sofkau.mongo.helper.AdapterOperations;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public class MongoRepositoryAdapter extends AdapterOperations<Carta, CartaDocument, String, MongoDBRepository>
 implements CartaRepository
{

    public MongoRepositoryAdapter(MongoDBRepository repository, ObjectMapper mapper) {
        /**
         *  Could be use mapper.mapBuilder if your domain model implement builder pattern
         *  super(repository, mapper, d -> mapper.mapBuilder(d,ObjectModel.ObjectModelBuilder.class).build());
         *  Or using mapper.map with the class of the object model
         */
        super(repository, mapper, d -> mapper.map(d, Carta.class));
    }

    @Override
    public Mono<Carta> findById(Carta carta) {
        return null;
    }

    @Override
    public Mono<Carta> update(String id, Carta carta) {
        carta.setId(id);

        return repository
                .save(new CartaDocument(carta.id(),carta.nombre(), carta.xp(), carta.imagen()))
                .flatMap(c -> Mono.just(carta));
    }

}
