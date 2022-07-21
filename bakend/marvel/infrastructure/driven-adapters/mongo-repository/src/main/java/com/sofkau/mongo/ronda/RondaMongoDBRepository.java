package com.sofkau.mongo.ronda;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;

public interface RondaMongoDBRepository extends ReactiveMongoRepository<RondaDocument, String>, ReactiveQueryByExampleExecutor<RondaDocument> {
}
