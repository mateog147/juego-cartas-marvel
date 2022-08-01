package com.sofkau.usecase.partida.repartircartas;

import com.sofkau.model.carta.Carta;
import com.sofkau.model.jugador.Jugador;
import com.sofkau.model.jugador.gateways.JugadorRepository;
import com.sofkau.model.mazo.Mazo;
import com.sofkau.model.partida.Partida;
import com.sofkau.model.ronda.Ronda;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RepartirCartasUseCaseTest {

    @InjectMocks
    RepartirCartasUseCase useCase;

    @Mock
    JugadorRepository repository;

    @Test
    private void respartir_cartas_a_jugadores_test(){

        Jugador jugador1 = new Jugador("j1","uid1", "jugador1", 0, new ArrayList<>());
        Jugador jugador2 = new Jugador("j2","uid2", "jugador2", 0, new ArrayList<>());
        List<Jugador> jugadoresPartida = List.of(jugador1, jugador2);
        List<String> jugadoresIds = List.of(jugador1.id(), jugador2.id());

        Carta carta1 = new Carta("c1", "carta1", 10, "xxx");
        Carta carta2 = new Carta("c2", "carta2", 20, "xxx");
        Carta carta3 = new Carta("c3", "carta3", 30, "xxx");
        Carta carta4 = new Carta("c4", "carta4", 40, "xxx");
        Carta carta5 = new Carta("c5", "carta5", 50, "xxx");
        Carta carta6 = new Carta("c6", "carta6", 60, "xxx");
        Carta carta7 = new Carta("c7", "carta7", 70, "xxx");
        Carta carta8 = new Carta("c8", "carta8", 80, "xxx");
        Carta carta9 = new Carta("c9", "carta9", 90, "xxx");
        Carta carta10 = new Carta("c10", "carta10", 100, "xxx");
        Carta carta11 = new Carta("c11", "carta11", 110, "xxx");
        List<Carta> cartasMazo = List.of(carta1, carta2, carta3, carta4, carta5, carta6, carta7, carta8, carta9, carta10, carta11);
        Mazo mazo =  Mazo.getMazo(cartasMazo);

        Ronda ronda = new Ronda();

        Partida partida = new Partida("partidaId", new ArrayList<>(), ronda, mazo);

        when(repository.findAllById(Mockito.any())).thenReturn(Flux.fromIterable(jugadoresPartida));

        StepVerifier.create(useCase.repartir(partida, jugadoresIds))
                .expectNextMatches(partida1 -> )

    }




}