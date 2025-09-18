package service.clients;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import service.core.BankAccount;
import service.dto.AmountRequest;

@Service
public class BankClient {
    private final RestTemplate restTemplate;
    private final String bankServiceUrl;

    public BankClient(RestTemplateBuilder restTemplateBuilder, 
                      @Value("${bank.service.url:http://localhost:8080}") String bankServiceUrl) {
        this.restTemplate = restTemplateBuilder.build();
        this.bankServiceUrl = bankServiceUrl;
    }

    // Get account details
    public BankAccount getAccount(Integer clientId) {
        String url = bankServiceUrl + "/accounts/" + clientId;
        return restTemplate.getForObject(url, BankAccount.class);
    }

    // Deposit funds
    public void depositFunds(Integer clientId, Double amount) {
        String url = bankServiceUrl + "/accounts/" + clientId + "/deposit";
        AmountRequest request = new AmountRequest(amount);
        restTemplate.postForEntity(url, request, Void.class);
    }

    // Withdraw funds
    public void withdrawFunds(Integer clientId, Double amount) {
        String url = bankServiceUrl + "/accounts/" + clientId + "/withdraw";
        AmountRequest request = new AmountRequest(amount);
        restTemplate.postForEntity(url, request, Void.class);
    }

    // Get account balance
    public Double getBalance(Integer clientId) {
        String url = bankServiceUrl + "/accounts/" + clientId + "/balance";
        return restTemplate.getForObject(url, Double.class);
    }
}