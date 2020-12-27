package com.borchowiec.test;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.time.Duration;

@RestController
public class TestController {

    @GetMapping
    public Flux<String> get() {
        Flux<String> flux = Flux.just("first ", "second ", "third" ).delayElements(Duration.ofSeconds(1));
        flux.subscribe();
        return flux;
    }

}
