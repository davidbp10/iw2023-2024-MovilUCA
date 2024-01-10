package es.uca.iw.telefonuca.user.services;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import es.uca.iw.telefonuca.user.domain.User;
import reactor.core.publisher.Mono;

@Service
public class UserAPIService {

    private WebClient webClient = WebClient.create();

    public void consumeApi() {
        String baseUrl = "http://omr-simulator.us-east-1.elasticbeanstalk.com/api";
        String endpoint = "/users/{id}";

        // Para GET
        String id = "123"; // Reemplaza con el ID deseado
        String getUrl = baseUrl + endpoint.replace("{id}", id);
        Mono<String> getResponse = webClient.get()
                .uri(getUrl)
                .retrieve()
                .bodyToMono(String.class);
        System.out.println(getResponse.block());

        // Para POST
        User myObject = new User();
        // Establecer propiedades de myObject
        String postUrl = baseUrl + endpoint.replace("{id}", "");
        Mono<String> postResponse = webClient.post()
                .uri(postUrl)
                .bodyValue(myObject)
                .retrieve()
                .bodyToMono(String.class);
        System.out.println(postResponse.block());
    }
}

