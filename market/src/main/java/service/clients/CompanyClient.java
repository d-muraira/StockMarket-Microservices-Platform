package service.clients;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import service.core.CompanyDTO;
import service.core.TradeResponse;
import service.dto.ShareRequest;

@Service
public class CompanyClient {
    private final RestTemplate restTemplate;
    private final String companyServiceUrl;

    public CompanyClient(RestTemplateBuilder restTemplateBuilder,
                         @Value("${company.service.url:http://localhost:8081}") String companyServiceUrl) {
        this.restTemplate = restTemplateBuilder.build();
        this.companyServiceUrl = companyServiceUrl;
    }

    // Buy shares from a company
    public TradeResponse buyShares(Integer companyId, Integer clientId, Integer quantity) {
        String url = companyServiceUrl + "/companies/" + companyId + "/buy";
        ShareRequest request = new ShareRequest(clientId, quantity);
        return restTemplate.postForObject(url, request, TradeResponse.class);
    }

    // Sell shares back to a company
    public TradeResponse sellShares(Integer companyId, Integer clientId, Integer quantity) {
        String url = companyServiceUrl + "/companies/" + companyId + "/sell";
        ShareRequest request = new ShareRequest(clientId, quantity);
        return restTemplate.postForObject(url, request, TradeResponse.class);
    }
}