package com.sofkau.mongo.ronda;

import com.sofkau.model.partida.Partida;
import com.sofkau.model.partida.gateways.PartidaRepository;
import com.sofkau.model.ronda.Ronda;
import com.sofkau.model.ronda.gateways.RondaRepository;
import com.sofkau.mongo.helper.AdapterOperations;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;

@Repository
public class RondaMongoRepositoryAdapter extends AdapterOperations<Ronda, RondaDocument, String, RondaMongoDBRepository>
 implements RondaRepository
{

    public RondaMongoRepositoryAdapter(RondaMongoDBRepository repository, ObjectMapper mapper) {
        /**
         *  Could be use mapper.mapBuilder if your domain model implement builder pattern
         *  super(repository, mapper, d -> mapper.mapBuilder(d,ObjectModel.ObjectModelBuilder.class).build());
         *  Or using mapper.map with the class of the object model
         */
        super(repository, mapper, d -> mapper.map(d, Ronda.class));
    }



}
