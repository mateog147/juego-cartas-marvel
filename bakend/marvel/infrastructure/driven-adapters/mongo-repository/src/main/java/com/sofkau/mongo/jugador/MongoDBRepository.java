package com.sofkau.mongo.jugador;

import com.sofkau.mongo.carta.CartaDocument;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;

public interface MongoDBRepository extends ReactiveMongoRepository<JugadorDocument, String>, ReactiveQueryByExampleExecutor<JugadorDocument> {
}
