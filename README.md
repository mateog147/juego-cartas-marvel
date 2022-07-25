
#  Challenge: Full Stack Game
Juego de Super Héroes
### Descripción: 
En este juego parte de un mazo de tarjetas de superhéroes de Marvel, vas a
encontrar 108 personajes con diferentes estilos y formas, algunas de estas tarjetas son
personajes individuales y otras son tarjetas con un grupo de personajes. Cada tarjeta tiene 
un Xp que representa su poder.
Luego de tener todas las tarjetas sistematizadas, con sus características y/o poderes, se
debe diseñar un juego de máximo 6 jugadores y mínimo 2 jugadores. El juego consiste en
repartir aleatoriamente 5 tarjetas a los 6 jugadores o a cada jugador creado previamente. El
juego consiste en que cada jugador apuesta sus tarjetas y gana el juego el que tenga todas
las tarjetas. Cada jugador apuesta basado en sus baraja de tarjetas y gana la partida la
tarjeta que tenga mayor XP.


### Solcución: 
Inicialmente planteamos el modelo de dominio sobre el cual fundamentariamos la aplicación, de 
este diseño hay que complementar que carta es una copia traida del maestro de cartas y que jugador
esta ligado a un copia del documento de la base de datos ligado al usuario de firebase:

![Modelo de dominio](https://i.ibb.co/PF30S3n/marvel-game-Modelo-de-dominio.jpg)


### Implementación: 

La solucion se hizo con una arquitectura desacoplada, con el backend en spring-boot, el frontend
en angular y la base de datos No-SQL en Mongo-db

El backend tiene una arquitectura de n capas y fue construido usando el arquetipo de bancolombia
Clean Architecture: puedes leer más sobre este proyecto en: https://medium.com/bancolombia-tech/clean-architecture-aislando-los-detalles-4f9530f35d7a

Tambien se implemento web flux para exponer un servicio tipo API REST, que 
maneja flujos reactivos del lado del backend.

Del lado del Front hicimos un proyecto Angular, que consume los la API, esta cuenta tambien con un
servicio con firebase que permite logearse e iniciar sesion con google o regustrarse con un correo.

Todos los datos sensibles de inicio de sesión son manejados en firebase y en la base de datos 
solo se almacenan los datos relevantes para el juego.


## Correr Local

Clone el proyecto

```bash
  git clone https://github.com/mateog147/juego-cartas-marvel.git
```

Abra la carpeta frontend

```bash
  cd frontend
```

Instale las dependencias del frontend

```bash
  npm install
```

Inicie el servidor del frontend, que correra en el puerto http://localhost:4200/

```bash
  npm run start
```

Regrese a la carpeta raiz y vaya a la carpeta marvel.
```bash
  cd ../bakend/marvel
```

Abrala Abrala con un editor que soporte gradle y permita que se inicialicen las 
dependencias.

Corra el proyecto desde el archivo MainApplication en el modulo Applications.

```bash
  com.sofkau.MainApplication.java
```

Inicie el servidor del backend que correra en el puerto http://localhost:8080


## Autores
- [@juanir33](https://github.com/juanir33) - Juan Ramallo

- [@mateog147](https://github.com/mateog147) - Mateo Gutiérrez

- [@mmaurogg](https://github.com/mmaurogg) - Manuel Mauricio Gomez


