package service.clients;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import service.core.Client;

@Service
public class PlayerClient {
    private final RestTemplate restTemplate;

    public PlayerClient(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    public Client getPlayer(int id) {
        String url = "http://localhost:8083/players/" + id;
        return restTemplate.getForObject(url, Client.class);
    }

}
