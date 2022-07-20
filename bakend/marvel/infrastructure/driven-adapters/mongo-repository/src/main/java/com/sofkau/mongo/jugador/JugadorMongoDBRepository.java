package com.sofkau.mongo.jugador;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;

public interface JugadorMongoDBRepository extends ReactiveMongoRepository<JugadorDocument, String>, ReactiveQueryByExampleExecutor<JugadorDocument> {
}
