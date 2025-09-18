package service.clients;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

import service.core.Company;
import service.core.CompanyDTO;
import service.core.Trade;
import service.dto.BuyRequest;
import service.dto.SellRequest;

import java.util.List;

/**
 * Client for interacting with the Market Service
 */
@Service
public class MarketServiceClient {
    private final RestTemplate restTemplate;
    
    @Value("${services.market.url:http://localhost:8082}")
    private String marketServiceUrl;
    
    /**
     * Constructor with dependency injection
     */
    @Autowired
    public MarketServiceClient(RestTemplateBuilder restTemplateBuilder, ObjectMapper objectMapper) {
        this.restTemplate = restTemplateBuilder
            .messageConverters(new MappingJackson2HttpMessageConverter(objectMapper))
            .build();
    }
    
    /**
     * Get the market service URL
     */
    public String getMarketServiceUrl() {
        return marketServiceUrl;
    }
    
    /**
     * Get all companies in the market
     */
    public List<CompanyDTO> getAllCompanies() {
        String url = marketServiceUrl + "/company-dtos";
        ResponseEntity<List<CompanyDTO>> response = restTemplate.exchange(
            url,
            HttpMethod.GET,
            null,
            new ParameterizedTypeReference<List<CompanyDTO>>() {}
        );
        
        if (response.getStatusCode() == HttpStatus.OK) {
            return response.getBody();
        }
        
        throw new RuntimeException("Failed to get companies: " + response.getStatusCode());
    }
    
    /**
     * Get a specific company by ID
     */
    public CompanyDTO getCompany(int companyId) {
        String url = marketServiceUrl + "/company-dtos/" + companyId;
        ResponseEntity<CompanyDTO> response = restTemplate.getForEntity(url, CompanyDTO.class);
        
        if (response.getStatusCode() == HttpStatus.OK) {
            return response.getBody();
        }
        
        throw new RuntimeException("Failed to get company: " + response.getStatusCode());
    }
    
    /**
     * Buy shares of a company
     */
    public Trade buyShares(int clientId, int companyId, int quantity) {
        String url = marketServiceUrl + "/companies/" + companyId + "/buy";
        
        // Create a proper DTO for the request
        BuyRequest request = new BuyRequest(clientId, quantity);
        
        ResponseEntity<Trade> response = restTemplate.postForEntity(
            url, request, Trade.class);
        
        if (response.getStatusCode() == HttpStatus.OK) {
            return response.getBody();
        }
        
        throw new RuntimeException("Failed to buy shares: " + response.getStatusCode());
    }
    
    /**
     * Sell shares of a company
     */
    public Trade sellShares(int clientId, int companyId, int quantity) {
        String url = marketServiceUrl + "/companies/" + companyId + "/sell";
        
        // Create a proper DTO for the request
        SellRequest request = new SellRequest(clientId, quantity);
        
        ResponseEntity<Trade> response = restTemplate.postForEntity(
            url, request, Trade.class);
        
        if (response.getStatusCode() == HttpStatus.OK) {
            return response.getBody();
        }
        
        throw new RuntimeException("Failed to sell shares: " + response.getStatusCode());
    }
    
    /**
     * Get trade history for a specific company
     */
    public List<Trade> getCompanyTradeHistory(int companyId) {
        String url = marketServiceUrl + "/trades?companyId=" + companyId;
        ResponseEntity<List<Trade>> response = restTemplate.exchange(
            url,
            HttpMethod.GET,
            null,
            new ParameterizedTypeReference<List<Trade>>() {}
        );
        
        if (response.getStatusCode() == HttpStatus.OK) {
            return response.getBody();
        }
        
        throw new RuntimeException("Failed to get trade history: " + response.getStatusCode());
    }
    
    /**
     * Get all trade history
     */
    public List<Trade> getAllTradeHistory() {
        String url = marketServiceUrl + "/trades";
        ResponseEntity<List<Trade>> response = restTemplate.exchange(
            url,
            HttpMethod.GET,
            null,
            new ParameterizedTypeReference<List<Trade>>() {}
        );
        
        if (response.getStatusCode() == HttpStatus.OK) {
            return response.getBody();
        }
        
        throw new RuntimeException("Failed to get all trade history: " + response.getStatusCode());
    }
    
    /**
     * Get the current share price for a company
     */
    public double getCompanySharePrice(int companyId) {
        CompanyDTO company = getCompany(companyId);
        return company.sharePrice();
    }
}