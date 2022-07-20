package com.sofkau.mongo.carta;

import com.sofkau.mongo.jugador.JugadorDocument;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;

public interface MongoDBRepository extends ReactiveMongoRepository<CartaDocument, String>, ReactiveQueryByExampleExecutor<CartaDocument> {
}
