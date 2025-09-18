package service.clients;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import service.core.BankAccount;
import java.util.Map;
import java.util.HashMap;

/**
 * Client for interacting with the Banking Service
 */
@Service
public class BankServiceClient {
    private final RestTemplate restTemplate;
    
    @Value("${services.bank.url:http://localhost:8080}")
    private String bankServiceUrl;
    
    /**
     * Constructor with dependency injection
     */
    @Autowired
    public BankServiceClient(RestTemplateBuilder restTemplateBuilder, ObjectMapper objectMapper) {
        this.restTemplate = restTemplateBuilder
            .messageConverters(new MappingJackson2HttpMessageConverter(objectMapper))
            .build();
    }
    
    /**
     * Create a new bank account for a client
     */
    public BankAccount createAccount(int clientId, String accountName, double initialFunds) {
        String url = bankServiceUrl + "/accounts";
        
        Map<String, Object> requestPayload = new HashMap<>();
        requestPayload.put("clientId", clientId);
        requestPayload.put("accountName", accountName);
        requestPayload.put("initialFunds", initialFunds);
        
        ResponseEntity<BankAccount> response = restTemplate.postForEntity(
            url, requestPayload, BankAccount.class);
        
        if (response.getStatusCode() == HttpStatus.CREATED || 
            response.getStatusCode() == HttpStatus.OK) {
            return response.getBody();
        }
        
        throw new RuntimeException("Failed to create bank account: " + response.getStatusCode());
    }
    
    /**
     * Get a bank account by client ID
     */
    public BankAccount getAccount(int clientId) {
        String url = bankServiceUrl + "/accounts/" + clientId;
        ResponseEntity<BankAccount> response = restTemplate.getForEntity(url, BankAccount.class);
        
        if (response.getStatusCode() == HttpStatus.OK) {
            return response.getBody();
        }
        
        throw new RuntimeException("Failed to get bank account: " + response.getStatusCode());
    }
    
    /**
     * Deposit funds into a client's account
     */
    public void depositFunds(int clientId, double amount) {
        String url = bankServiceUrl + "/accounts/" + clientId + "/deposit";
        
        Map<String, Object> requestPayload = new HashMap<>();
        requestPayload.put("amount", amount);
        
        ResponseEntity<Void> response = restTemplate.postForEntity(url, requestPayload, Void.class);
        
        if (response.getStatusCode() != HttpStatus.OK) {
            throw new RuntimeException("Failed to deposit funds: " + response.getStatusCode());
        }
    }
    
    /**
     * Withdraw funds from a client's account
     */
    public void withdrawFunds(int clientId, double amount) {
        String url = bankServiceUrl + "/accounts/" + clientId + "/withdraw";
        
        Map<String, Object> requestPayload = new HashMap<>();
        requestPayload.put("amount", amount);
        
        ResponseEntity<Void> response = restTemplate.postForEntity(url, requestPayload, Void.class);
        
        if (response.getStatusCode() != HttpStatus.OK) {
            throw new RuntimeException("Failed to withdraw funds: " + response.getStatusCode());
        }
    }
    
    /**
     * Get the balance of a client's account
     */
    public double getBalance(int clientId) {
        String url = bankServiceUrl + "/accounts/" + clientId + "/balance";
        ResponseEntity<Double> response = restTemplate.getForEntity(url, Double.class);
        
        if (response.getStatusCode() == HttpStatus.OK) {
            return response.getBody();
        }
        
        throw new RuntimeException("Failed to get balance: " + response.getStatusCode());
    }
}