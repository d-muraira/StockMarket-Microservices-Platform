package service.core;

import java.io.Serializable;

public class TradeResponse implements Serializable {
    private boolean success;
    private int quantity;
    private double tradeValue;
    
    public TradeResponse(boolean success, int quantity, double tradeValue) {
        this.success = success;
        this.quantity = quantity;
        this.tradeValue = tradeValue;
    }
    
    // Default constructor for serialization
    public TradeResponse() {}
    
    // Getters and setters
    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public double getTradeValue() { return tradeValue; }
    public void setTradeValue(double tradeValue) { this.tradeValue = tradeValue; }
}