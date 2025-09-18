package service.controllers;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import service.clients.BankServiceClient;
import service.clients.MarketServiceClient;
import service.core.*;
import service.services.PlayerService;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;

/**
 * REST Controller for Player Service
 */
@RestController
public class PlayerController {
    private final PlayerService playerService;
    private final BankServiceClient bankClient;
    private final MarketServiceClient marketClient;
    private final RestTemplate restTemplate; // Create directly
    
    @Autowired
    public PlayerController(PlayerService playerService, 
                           BankServiceClient bankClient, 
                           MarketServiceClient marketClient) {
        this.playerService = playerService;
        this.bankClient = bankClient;
        this.marketClient = marketClient;
        this.restTemplate = new RestTemplate(); // Create RestTemplate directly
    }
    
    @GetMapping(path="/", produces="application/json")
    public ResponseEntity<String> testEndpoint() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body("Player Service is running");
    }
    
    /**
     * Get all players
     */
    @GetMapping(path="/players", produces="application/json")
    public ResponseEntity<List<Client>> getAllPlayers() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(playerService.getAllPlayers());
    }
    
    /**
     * Get a specific player by ID
     */
    @GetMapping(path="/players/{playerId}", produces="application/json")
    public ResponseEntity<Client> getPlayer(@PathVariable Integer playerId) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(playerService.getPlayer(playerId));
    }
    
    /**
     * Register a new player
     */
    @PostMapping(path="/players", consumes="application/json", produces="application/json")
    public ResponseEntity<Client> registerPlayer(HttpServletRequest request,
                                                @RequestBody Map<String, Object> registrationData) {
        String name = (String) registrationData.get("name");
        double initialFunds = registrationData.containsKey("initialFunds") ? 
                             ((Number) registrationData.get("initialFunds")).doubleValue() : 0.0;
        
        // Generate a unique player ID first
        int playerId = (int) (System.currentTimeMillis() % 100000);
        
        // Create a bank account using the same ID as the player
        BankAccount account = bankClient.createAccount(playerId, name + "'s Account", initialFunds);
        
        // Create and register the player with the same ID
        Client player = new Client(playerId, name, account);
        playerService.registerPlayer(player);
        
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .header("Content-Location", request.getRequestURI() + "/" + playerId)
                .body(player);
    }
    
    /**
     * Update player information
     */
    @PutMapping(path="/players/{playerId}", consumes="application/json", produces="application/json")
    public ResponseEntity<Client> updatePlayer(@PathVariable Integer playerId,
                                              @RequestBody Client updatedPlayer) {
        // Ensure the path parameter matches the player ID in the request body
        updatedPlayer.setId(playerId);
        
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(playerService.updatePlayer(playerId, updatedPlayer));
    }
    
    /**
     * Remove a player from the system
     */
    @DeleteMapping(path="/players/{playerId}")
    public ResponseEntity<Void> removePlayer(@PathVariable Integer playerId) {
        playerService.removePlayer(playerId);
        
        return ResponseEntity
                .noContent()
                .build();
    }
    
    /**
     * Get a player's portfolio
     */
    @GetMapping(path="/players/{playerId}/portfolio", produces="application/json")
    public ResponseEntity<List<ShareHolding>> getPlayerPortfolio(@PathVariable Integer playerId) {
        Client player = playerService.getPlayer(playerId);
        
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(player.getPortfolio());
    }
    
    /**
     * Get the total value of a player's portfolio
     */
    @GetMapping(path="/players/{playerId}/portfolio/value", produces="application/json")
    public ResponseEntity<Map<String, Double>> getPortfolioValue(@PathVariable Integer playerId) {
        double portfolioValue = playerService.getPlayerPortfolioValue(playerId);
        
        Map<String, Double> response = new HashMap<>();
        response.put("portfolioValue", portfolioValue);
        
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }
    
    /**
     * Get a player's total assets (bank balance + portfolio value)
     */
    @GetMapping(path="/players/{playerId}/assets", produces="application/json")
    public ResponseEntity<Map<String, Object>> getPlayerAssets(@PathVariable Integer playerId) {
        Client player = playerService.getPlayer(playerId);
        double bankBalance = player.getBankAccount().getBalance();
        double portfolioValue = playerService.getPlayerPortfolioValue(playerId);
        double totalAssets = playerService.getPlayerTotalAssets(playerId);
        
        Map<String, Object> response = new HashMap<>();
        response.put("bankBalance", bankBalance);
        response.put("portfolioValue", portfolioValue);
        response.put("totalAssets", totalAssets);
        
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }
    
    /**
     * Buy shares for a player
     */
    @PostMapping(path="/players/{playerId}/buy", consumes="application/json", produces="application/json")
    public ResponseEntity<Map<String, Object>> buyShares(@PathVariable Integer playerId,
                                                      @RequestBody Map<String, Object> purchaseData) {
        try {
            int companyId = ((Number) purchaseData.get("companyId")).intValue();
            int quantity = ((Number) purchaseData.get("quantity")).intValue();
            
            // Get current share price using CompanyDTO instead of Company
            CompanyDTO company = marketClient.getCompany(companyId);
            double sharePrice = company.sharePrice();
            double totalCost = sharePrice * quantity;
            
            // Get player and check bank balance
            Client player = playerService.getPlayer(playerId);
            double bankBalance = player.getBankAccount().getBalance();
            
            if (bankBalance < totalCost) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("error", "Insufficient funds");
                errorResponse.put("available", bankBalance);
                errorResponse.put("required", totalCost);
                
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body(errorResponse);
            }
            
            // Execute trade
            Trade trade = marketClient.buyShares(playerId, companyId, quantity);
            
            // Withdraw funds from bank account
            bankClient.withdrawFunds(playerId, totalCost);
            
            // Update player's portfolio
            ShareHolding newHolding = new ShareHolding(
                company.id(),
                company.name(),
                totalCost,
                quantity
            );
            player.addShareHolding(newHolding);
            playerService.updatePlayer(playerId, player);
            
            // Create response
            Map<String, Object> response = new HashMap<>();
            response.put("trade", trade);
            response.put("newBalance", player.getBankAccount().getBalance() - totalCost);
            response.put("portfolioValue", player.getPortfolioValue());
            
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(response);
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Failed to buy shares: " + e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(errorResponse);
        }
    }
    
    /**
     * Sell shares for a player
     */
    @PostMapping(path="/players/{playerId}/sell", consumes="application/json", produces="application/json")
    public ResponseEntity<Map<String, Object>> sellShares(@PathVariable Integer playerId,
                                                      @RequestBody Map<String, Object> saleData) {
        try {
            int companyId = ((Number) saleData.get("companyId")).intValue();
            int quantity = ((Number) saleData.get("quantity")).intValue();
            
            // Get current share price using CompanyDTO
            CompanyDTO company = marketClient.getCompany(companyId);
            double sharePrice = company.sharePrice();
            double totalValue = sharePrice * quantity;
            
            // Get player and check share holdings
            Client player = playerService.getPlayer(playerId);
            int availableShares = player.getShareQuantity(companyId);
            
            if (availableShares < quantity) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("error", "Insufficient shares");
                errorResponse.put("available", availableShares);
                errorResponse.put("requested", quantity);
                
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body(errorResponse);
            }
            
            // Execute trade
            Trade trade = marketClient.sellShares(playerId, companyId, quantity);
            
            // Deposit funds into bank account
            bankClient.depositFunds(playerId, totalValue);
            
            // Update player's portfolio
            player.removeShares(companyId, quantity);
            playerService.updatePlayer(playerId, player);
            
            // Create response
            Map<String, Object> response = new HashMap<>();
            response.put("trade", trade);
            response.put("newBalance", player.getBankAccount().getBalance() + totalValue);
            response.put("portfolioValue", player.getPortfolioValue());
            
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(response);
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Failed to sell shares: " + e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(errorResponse);
        }
    }
    
    /**
     * Get market information (all companies)
     */
    @GetMapping(path="/market/companies", produces="application/json")
    public ResponseEntity<List<CompanyDTO>> getMarketCompanies() {
        try {
            String url = marketClient.getMarketServiceUrl() + "/company-dtos";
            ResponseEntity<List<CompanyDTO>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<CompanyDTO>>() {}
            );
            
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(response.getBody());
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
    
    /**
     * Get information about a specific company
     */
    @GetMapping(path="/market/companies/{companyId}", produces="application/json")
    public ResponseEntity<CompanyDTO> getCompanyInfo(@PathVariable Integer companyId) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(marketClient.getCompany(companyId));
    }
}