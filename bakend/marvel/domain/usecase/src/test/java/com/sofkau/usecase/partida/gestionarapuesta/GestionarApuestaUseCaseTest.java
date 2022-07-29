package com.sofkau.usecase.partida.gestionarapuesta;

import com.sofkau.model.carta.Carta;
import com.sofkau.model.jugador.Jugador;
import com.sofkau.model.mazo.Mazo;
import com.sofkau.model.partida.Partida;
import com.sofkau.model.ronda.Ronda;
import com.sofkau.model.ronda.values.Apuesta;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.test.StepVerifier;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class GestionarApuestaUseCaseTest {

    @InjectMocks
    GestionarApuestaUseCase useCase;

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

        Ronda ronda = new Ronda();

        Carta cartaApuesta = new Carta("cartaId", "cartaApostada", 1000, "xxx");
        Apuesta apuesta = new Apuesta(jugador1.id(), cartaApuesta);

        Partida partida = new Partida("partidaId", jugadoresPartida, ronda, mazo);

        // Verificar que la apuesta se encuentre en la ronda
        StepVerifier.create(useCase.gestionarApuesta(partida, apuesta))
                .expectNextMatches(partida1 -> partida1.getRonda()
                        .getApuestas()
                        .get(0)
                        .getCarta()
                        .nombre()
                        .equals("cartaApostada"))
                .expectComplete()
                .verify();
    }

}