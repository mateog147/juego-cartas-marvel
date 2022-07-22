package com.sofkau.mongo.partida;

import com.sofkau.model.jugador.Jugador;
import com.sofkau.model.partida.Partida;
import com.sofkau.model.partida.gateways.PartidaRepository;
import com.sofkau.mongo.helper.AdapterOperations;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public class PartidaMongoRepositoryAdapter extends AdapterOperations<Partida, PartidaDocument, String, PartidaMongoDBRepository>
 implements PartidaRepository
{

    public PartidaMongoRepositoryAdapter(PartidaMongoDBRepository repository, ObjectMapper mapper) {
        /**
         *  Could be use mapper.mapBuilder if your domain model implement builder pattern
         *  super(repository, mapper, d -> mapper.mapBuilder(d,ObjectModel.ObjectModelBuilder.class).build());
         *  Or using mapper.map with the class of the object model
         */
        super(repository, mapper, d -> mapper.map(d, Partida.class));
    }


    @Override
    public Flux<Jugador> findAllById(Iterable<String> ids) {
        return repository.findAllById(ids)
                .map(document -> mapper.map(document, Jugador.class));
    }
}
