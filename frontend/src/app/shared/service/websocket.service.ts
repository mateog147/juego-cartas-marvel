// src\app\services\websocket.service.ts
import { Injectable } from "@angular/core";
import { Observable, Observer, Subject } from 'rxjs';
import { AnonymousSubject } from 'rxjs/internal/Subject';
import { map } from 'rxjs/operators';
import { ApuestaModel } from "src/app/interface/apuesta.interface";

const CHAT_URL = "ws://localhost:8080/ws/apuestas";



@Injectable()
export class WebsocketService {
    private subject!: AnonymousSubject<MessageEvent>;
    public messages: Subject<any>;

    constructor() {
        this.messages = <Subject<any>>this.connect(CHAT_URL).pipe(
            map(
                (response: MessageEvent): any => {
                    console.log(response.data);
                    return JSON.parse(response.data);
                }
            )
        );
    }

    public connect(url: string): AnonymousSubject<MessageEvent> {
        if (!this.subject) {
            this.subject = this.create(url);
            console.log("Successfully connected: " + url);
        }
        return this.subject;
    }

    private create(url: string): AnonymousSubject<MessageEvent> {
        let ws = new WebSocket(url);
        let observable = new Observable((obs: Observer<MessageEvent>) => {
            ws.onmessage = obs.next.bind(obs);
            ws.onerror = obs.error.bind(obs);
            ws.onclose = obs.complete.bind(obs);
            return ws.close.bind(ws);
        });
        let observer = {
            error: () =>{console.log('eroor')},
            complete: ()=>{},
            next: (data: Object) => {
                console.log('apuesta hecha', data);
                if (ws.readyState === WebSocket.OPEN) {
                    ws.send(JSON.stringify(data));
                }
            }
        };
        return new AnonymousSubject<MessageEvent>(observer, observable);
    }
}
