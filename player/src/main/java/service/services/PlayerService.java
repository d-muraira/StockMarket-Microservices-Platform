package service.services;

import org.springframework.stereotype.Service;
import service.core.Client;
import service.exceptions.PlayerNotFoundException;
import service.core.ShareHolding;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Service class for managing player-related operations
 */
@Service
public class PlayerService {
    private Map<Integer, Client> players;
    
    /**
     * Constructor with dependency injection
     */
    public PlayerService(Map<Integer, Client> players) {
        this.players = players;
    }
    
    /**
     * Default constructor
     */
    public PlayerService() {
        this.players = new ConcurrentHashMap<>();
    }
    
    /**
     * Register a new player
     */
    public Client registerPlayer(Client player) {
        int playerId = player.getId();
        players.putIfAbsent(playerId, player);
        return player;
    }
    
    /**
     * Get a player by ID
     */
    public Client getPlayer(int playerId) {
        Client player = players.get(playerId);
        if (player == null) {
            throw new PlayerNotFoundException("Player with ID " + playerId + " not found");
        }
        return player;
    }
    
    /**
     * Get all registered players
     */
    public List<Client> getAllPlayers() {
        return new ArrayList<>(players.values());
    }
    
    /**
     * Update player information
     */
    public Client updatePlayer(int playerId, Client updatedPlayer) {
        if (!players.containsKey(playerId)) {
            throw new PlayerNotFoundException("Player with ID " + playerId + " not found");
        }
        players.put(playerId, updatedPlayer);
        return updatedPlayer;
    }
    
    /**
     * Remove a player from the system
     */
    public void removePlayer(int playerId) {
        if (!players.containsKey(playerId)) {
            throw new PlayerNotFoundException("Player with ID " + playerId + " not found");
        }
        players.remove(playerId);
    }
    
    /**
     * Update a player's portfolio with a new share holding
     */
    public Client updatePlayerPortfolio(int playerId, ShareHolding shareHolding) {
        Client player = getPlayer(playerId);
        player.addShareHolding(shareHolding);
        return player;
    }
    
    /**
     * Calculate and return the total portfolio value for a player
     */
    public double getPlayerPortfolioValue(int playerId) {
        Client player = getPlayer(playerId);
        return player.getPortfolioValue();
    }
    
    /**
     * Calculate and return the total assets for a player (bank balance + portfolio value)
     */
    public double getPlayerTotalAssets(int playerId) {
        Client player = getPlayer(playerId);
        player.updateTotalAssets();
        return player.getTotalAssets();
    }
}