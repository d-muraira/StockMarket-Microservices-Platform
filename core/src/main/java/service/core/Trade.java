package service.core;

import java.io.Serializable;

public class Trade implements Serializable {
    private static int COUNTER = 3000;
    private int id;
    private int clientId;
    private int companyId;
    private String companyName;
    private double sharePrice;
    private int quantityPurchased;
    // maybe private String purchaseTime;

    public Trade(int clientId, int companyId, String companyName, double sharePrice, int quantityPurchased) {
        this.id = COUNTER++;
        this.clientId = clientId;
        this.companyId = companyId;
        this.sharePrice = sharePrice;
        this.quantityPurchased = quantityPurchased;
        this.companyName = companyName;
    }
    
    public Trade() {
        this.id = COUNTER++;
    }
    
    // Add getter methods
    public int getId() {
        return id;
    }
    
    public int getClientId() {
        return clientId;
    }
    
    public int getCompanyId() {
        return companyId;
    }
    
    public String getCompanyName() {
        return companyName;
    }
    
    public double getSharePrice() {
        return sharePrice;
    }
    
    public int getQuantityPurchased() {
        return quantityPurchased;
    }
    
    // Add setter methods if needed
    public void setClientId(int clientId) {
        this.clientId = clientId;
    }
    
    public void setCompanyId(int companyId) {
        this.companyId = companyId;
    }
    
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
    
    public void setSharePrice(double sharePrice) {
        this.sharePrice = sharePrice;
    }
    
    public void setQuantityPurchased(int quantityPurchased) {
        this.quantityPurchased = quantityPurchased;
    }
}