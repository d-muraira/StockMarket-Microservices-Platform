package service.dto;

public class AmountRequest {
    private Double amount;
    
    // Default constructor
    public AmountRequest() {}
    
    public AmountRequest(Double amount) {
        this.amount = amount;
    }
    
    // Getters and setters
    public Double getAmount() { return amount; }
    public void setAmount(Double amount) { this.amount = amount; }
}