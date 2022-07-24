import { Card } from "src/app/components/card/card.component";

export interface Jugador{
    id?: string;
    nombre: string;
    uid: string;
    cartas: Card[];
    puntaje: number
}