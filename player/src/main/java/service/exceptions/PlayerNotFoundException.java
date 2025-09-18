package service.exceptions;

/**
 * Exception thrown when a player is not found in the system
 */
public class PlayerNotFoundException extends RuntimeException {
    public PlayerNotFoundException(String message) {
        super(message);
    }
}