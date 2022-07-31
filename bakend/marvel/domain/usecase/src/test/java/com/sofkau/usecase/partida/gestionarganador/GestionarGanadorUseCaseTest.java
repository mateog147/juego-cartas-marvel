package com.sofkau.usecase.partida.gestionarganador;

import com.sofkau.model.carta.Carta;
import com.sofkau.model.jugador.Jugador;
import com.sofkau.model.jugador.gateways.JugadorRepository;
import com.sofkau.model.mazo.Mazo;
import com.sofkau.model.partida.Partida;
import com.sofkau.model.ronda.Ronda;
import com.sofkau.model.ronda.values.Apuesta;
import com.sofkau.usecase.ronda.ganadorronda.GanadorRondaUseCase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GestionarGanadorUseCaseTest {

    @InjectMocks
    GestionarGanadorUseCase useCase;

    @Mock
    JugadorRepository repository;
    @Mock
    GanadorRondaUseCase ganadorRondaUseCase;

    @Test
    public void gestionar_ganador_test(){
        //Arrange
        //Carta cartaJugador1 = new Carta("cj1", "cartaJugador1", 10, "xxx");
        Jugador jugador1 = new Jugador("j1","uid1", "jugador1", 0, new ArrayList<>());
        Carta cartaJugador2 = new Carta("cj2", "cartaJugador2", 20, "xxx");
        Jugador jugador2 = new Jugador("j2","uid2", "jugador2", 0, List.of(cartaJugador2));
        List<Jugador> jugadoresPartida = List.of(jugador1, jugador2);

        Carta cartaApuesta1 = new Carta("ca1", "cartaApostada1", 1000, "xxx");
        Apuesta apuesta1 = new Apuesta(jugador1.id(), cartaApuesta1);
        Carta cartaApuesta2 = new Carta("ca2", "cartaApostada2", 2000, "xxx");
        Apuesta apuesta2 = new Apuesta(jugador2.id(), cartaApuesta2);
        List<Apuesta> apuestas = List.of(apuesta1, apuesta2);
        Ronda ronda = new Ronda("rondaId", apuestas, 1, "", 60);

        Carta carta1 = new Carta("1", "Hulk", 100, "xxx");
        Carta carta2 = new Carta("2", "Spiderman", 50, "xxx");
        List<Carta> cartasMazo = List.of(carta1, carta2);
        Mazo mazo =  Mazo.getMazo(cartasMazo);

        Partida partida = new Partida("partidaId", jugadoresPartida, ronda, mazo);

        when(repository.findById(jugador2.id())).thenReturn(Mono.just(jugador2));
        //when(ganadorRondaUseCase.terminarRonda(Mockito.any(),Mockito.any()))
        //        .thenReturn(Mono.just(Ronda.builder().ultimoGanador("jugador2").build()));

        Partida partidaNueva = partida;
        useCase.gestionarGanador(partida)
                .flatMap(partida1 -> {
                    partidaNueva.toBuilder()
                            .jugadores(partida1.getJugadores())
                            .ronda(partida1.getRonda())
                            .build();
                    return Mono.just(partida1);
                });

        Jugador ganador = partidaNueva.getJugadores()
                .stream()
                .filter(jugadorGanador -> jugadorGanador.id().equals(jugador2.getId())).findFirst().get();
        List<Carta> cartaGanada = ganador.getCartas();

        System.out.println("hola"+ganador);
        //Assertions.assertEquals("cartaApostada1", cartaGanada.getNombre());

        // El ganador (jugador2) Recibe las cartas apostadas
        /*
        StepVerifier.create(useCase.gestionarGanador(partida))
                .expectNextMatches(partida1 -> partida1
                        .getJugadores()
                        .stream()
                        .filter(jugadorFiltrado -> jugadorFiltrado.equals(jugador2.id()))
                        .findAny()
                        .get()
                        .getCartas()
                        .stream()
                        .filter(cartaFiltrada -> cartaFiltrada.nombre().equals("cartaApostada1"))
                        .findAny()
                        .get()
                        .getNombre()
                        .equals("cartaApostada1")
                )
                .expectComplete()
                .verify();

         */


        // EL jugador sin cartas se retira de la partida





    }

}