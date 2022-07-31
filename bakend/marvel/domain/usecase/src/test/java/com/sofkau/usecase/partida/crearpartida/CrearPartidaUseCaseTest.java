package com.sofkau.usecase.partida.crearpartida;

import com.sofkau.model.carta.Carta;
import com.sofkau.model.carta.gateways.CartaRepository;
import com.sofkau.model.mazo.Mazo;
import com.sofkau.model.partida.Partida;
import com.sofkau.model.partida.gateways.PartidaRepository;
import com.sofkau.model.ronda.Ronda;
import com.sofkau.usecase.carta.mostrarcartas.MostrarCartasUseCase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CrearPartidaUseCaseTest {

    @InjectMocks
    CrearPartidaUseCase useCase;

    @Mock
    PartidaRepository partidaRepository;
    @Mock
    MostrarCartasUseCase mostrarCartasUseCase;

    @Test
    public void crear_partida(){
        Carta carta1 = new Carta("1", "Hulk", 100, "xxx");
        Carta carta2 = new Carta("2", "Spiderman", 50, "xxx");
        List<Carta> cartas = List.of(carta1, carta2);

        Ronda ronda = new Ronda();

        Mazo mazo =  Mazo.getMazo(cartas);

        Partida partida = Partida.builder()
                .mazo(mazo)
                .ronda(ronda)
                .build();

        when(mostrarCartasUseCase.mostrarCartas()).thenReturn(Flux.fromIterable(cartas));
        when(partidaRepository.save(Mockito.any(Partida.class)))
                .thenReturn(Mono.just(partida));

        StepVerifier.create(useCase.crearPartida())
                .expectNext(partida)
                .expectComplete()
                .verify();
    }
}