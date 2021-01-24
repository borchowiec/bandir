# Opis technologii
## Spring WebFlux
Spring WebFlux jest modułem Springa. Pozwala na budowanie aplikacji w oparciu o programowanie reaktywne. Dzieje się tak dzięki bibliotece Project Reactor o którą opiera się Spring WebFlux. 

## Netty vs Tomcat
Aplikacje zbudowane na Spring WebFlux korzystają z wbudowanego kontenera sieciowym Netty. W przeciwieństwie do Tomcata wspiera on operacje nieblokujące.

## Baza danych
W projekcie użyłem MongoDB. Niestety, nie możemy użyć typowych rozwiązań dla Springa jakimi są JDBC lub Hibernate. Wszelkie konektory lub ORMy muszą wspierać nieblokujące operacje. Popularnym wyborem jest Reactive Mongo które zostało użyte przeze mnie w tym projekcie.

## Gateway
Typowym rozwiązaniem jeśli chodzi o gateway w architekturach mikroserwisowych jest moduł Spring Cloud Netflix Zuul. Problem polega na tym, że nie wspiera on modułu WebFlux. Inną opcją jest moduł Spring Cloud Gateway. Jest on oparty o Spring WebFlux, dzięki czemu w pełni wspiera on programowanie reaktywne. Dodatkowo, jest on w pełni kompatybilny z takimi modułami jak Spring Cloud Netflix Eureka.

## Programowanie reaktywne
Programowanie reaktywne jest asynchronicznym paradygmatem programowania, polegający na przetwarzaniu strumieni danych w sposób nie blokujący. W skrócie odbywa się to na zasadzie oddelegowania danego zadania do póli wątków. Jeden wątek odbiera rządanie, po czym przekazuje je do puli wątków. W Spring WebFlux możemy to osiągnąć przy pomocy strumieni Mono (przetwarza jeden element) oraz Flux (przetwarza wiele elementów).

Zalety takiego rozwiązania możemy zaobserwować przy aplikacjach przetwarzających wiele żądań. Przykładowo, Apache Tomcat domyślnie zawiera pulę dwustu wątków. Stosując podejście Thread per Request, w przypadku gdy przekroczymy liczbę wątków, kolejne żądanie będzie musiało zaczekać na zwolnienie wątku. Taka sytuacja nie wystąpi w podejściu reaktywnym.

# Przykład działania
* Wchodzimy na stronę [https://localhost](https://localhost)
* Na starcie, zostaje nawiązane połączenie z serwisem "user" poprzez websocket.
* Przechodzimy do formularza rejestracji, wypełniamy dane i klikamy "SIGN UP".
* W tym momencie, żądanie zostało wysłane do gateway'a który następnie przekierowuje zapytanie do odpowiedniej instancji mikroserwisu. Do zapytania zostało dodane id sesji websocketu.
* Zapytanie jest przetwarzane w instancji mikroserwisu "user".
* Jeśli wszystko pójdzie dobrze, w odpowiedzi dostaniemy status 200.
* Dodatkowo, po wykonaniu wszystkich zadań, zostanie utworzony specjalny event z wiadomością zawierającą informacje o dodaniu użytkownika do bazy danych.
* Event zostaje przekazany do klasy obsługującej sesje websocket, w której wiadomość zostaje wysłana do użytkownika.
* Użytkownik otrzymuje wiadomość poprzez websocket i zostaje wyświetlone powiadomienie.
