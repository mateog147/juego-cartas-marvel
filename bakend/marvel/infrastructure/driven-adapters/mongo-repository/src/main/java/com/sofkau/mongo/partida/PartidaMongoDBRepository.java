package com.sofkau.mongo.partida;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;

public interface PartidaMongoDBRepository extends ReactiveMongoRepository<PartidaDocument, String>, ReactiveQueryByExampleExecutor<PartidaDocument> {
}
