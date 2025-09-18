package service.core;

import java.io.Serializable;

public class ShareHolding implements Serializable {
    private int companyId;
    private String companyName; // company
    private double value; // overall value value += (quantity * sharePrice)
    private int quantity;

    public ShareHolding(int companyId, String companyName, double value, int quantity) {
        this.companyId = companyId;
        this.companyName = companyName;
        this.value = value;
        this.quantity = quantity;
    }
    
    public ShareHolding() {}
    
    // Getters and setters
    public int getCompanyId() {
        return companyId;
    }
    
    public void setCompanyId(int companyId) {
        this.companyId = companyId;
    }
    
    public String getCompanyName() {
        return companyName;
    }
    
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
    
    public double getValue() {
        return value;
    }
    
    public void setValue(double value) {
        this.value = value;
    }
    
    public int getQuantity() {
        return quantity;
    }
    
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    
    // Utility methods
    public void increaseQuantity(int amount) {
        this.quantity += amount;
    }
    
    public void decreaseQuantity(int amount) {
        this.quantity = Math.max(0, this.quantity - amount);
    }
    
    public void addValue(double amount) {
        this.value += amount;
    }
}