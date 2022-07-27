package com.sofkau.usecase.jugador.consultarjugadores;


import com.sofkau.model.jugador.Jugador;
import com.sofkau.model.jugador.gateways.JugadorRepository;
import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@SpringBootTest(classes=ConsultarjugadoresUseCase.class)
@DataMongoTest
@Import(ConsultarjugadoresUseCase.class)
class ConsultarjugadoresUseCaseTest {

        private final JugadorRepository repository;
        private  final ConsultarjugadoresUseCase useCase;

    public ConsultarjugadoresUseCaseTest(@Autowired JugadorRepository repository,@Autowired ConsultarjugadoresUseCase useCase) {
        this.repository = repository;
        this.useCase = useCase;
    }

    @Test
        public void consultar(){

            Mono<Jugador> gurdados = repository.save(new Jugador("1","1","nombre",1,null));

            Flux<Jugador> consulta = useCase.consultarJugadores();

        StepVerifier.create(consulta)
                .expectNextMatches(jugador -> jugador.id().equals("1"))
                .thenCancel()
                .verify();
        }
}