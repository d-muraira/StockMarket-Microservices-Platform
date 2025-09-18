package service.dto;

public class ShareRequest {
    private Integer clientId;
    private Integer quantity;
    
    // Default constructor
    public ShareRequest() {}
    
    public ShareRequest(Integer clientId, Integer quantity) {
        this.clientId = clientId;
        this.quantity = quantity;
    }
    
    // Getters and setters
    public Integer getClientId() { return clientId; }
    public void setClientId(Integer clientId) { this.clientId = clientId; }
    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
}