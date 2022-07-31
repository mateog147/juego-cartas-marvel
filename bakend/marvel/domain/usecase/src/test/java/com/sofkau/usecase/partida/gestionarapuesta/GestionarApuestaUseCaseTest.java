package com.sofkau.usecase.partida.gestionarapuesta;

import com.sofkau.model.carta.Carta;
import com.sofkau.model.jugador.Jugador;
import com.sofkau.model.mazo.Mazo;
import com.sofkau.model.partida.Partida;
import com.sofkau.model.ronda.Ronda;
import com.sofkau.model.ronda.values.Apuesta;
import com.sofkau.usecase.ronda.recibirapuesta.RecibirapuestaUseCase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GestionarApuestaUseCaseTest {

    @InjectMocks
    GestionarApuestaUseCase useCase;

    @Mock
    RecibirapuestaUseCase recibirapuestaUseCase;

    @Test
    public void gestionar_apuesta_test(){

        //Arrange
        Carta carta1 = new Carta("1", "Hulk", 100, "xxx");
        Carta carta2 = new Carta("2", "Spiderman", 50, "xxx");
        List<Carta> cartasMazo = List.of(carta1, carta2);
        Mazo mazo =  Mazo.getMazo(cartasMazo);

        Carta cartaJugador1 = new Carta("cj1", "cartaJugador1", 10, "xxx");
        Jugador jugador1 = new Jugador("j1","uid1", "jugador1", 0, List.of(cartaJugador1));
        Carta cartaJugador2 = new Carta("cj2", "cartaJugador2", 20, "xxx");
        Jugador jugador2 = new Jugador("j1","uid2", "jugador2", 0, List.of(cartaJugador2));
        List<Jugador> jugadoresPartida = List.of(jugador1, jugador2);

        Carta cartaApostada = new Carta("cartaId", "cartaApostada", 1000, "xxx");
        Ronda ronda = new Ronda("1", List.of(new Apuesta(jugador2.id(), cartaApostada)), 1, "", 60);

        Apuesta apuesta = new Apuesta(jugador1.id(), cartaJugador1);

        Partida partida = new Partida("partidaId", jugadoresPartida, ronda, mazo);

        when(recibirapuestaUseCase.recibirApuesta(ronda.getId(), apuesta))
                .thenReturn(Mono.just(
                        ronda.toBuilder()
                                .apuestas(List.of(apuesta, new Apuesta(jugador2.id(), cartaApostada)))
                                .build()));


        //Verificar jugador1 eliminado

        // Verificar que la apuesta se encuentre en la ronda

        StepVerifier.create(useCase.gestionarApuesta(partida, apuesta))
                .expectNextMatches(partida1 -> partida1
                        .getRonda()
                        .getApuestas()
                        .stream().filter(apuestaFiltrada -> apuestaFiltrada.getJugadorId().equals(jugador1.id()))
                        .findFirst()
                        .get()
                        .getCarta()
                        .nombre()
                        .equals("cartaJugador1")
                )
                .expectComplete()
                .verify();


    }

}