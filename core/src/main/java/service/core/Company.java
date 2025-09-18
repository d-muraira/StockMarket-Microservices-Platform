package service.core;

import java.io.Serializable;

public abstract class Company implements Serializable {
    private static int COUNTER = 2000;
    private int id;
    private String name;
    private double sharePrice;
    private double shareMultiplier;
    private int numShares;

    public Company(String name, double sharePrice, double shareMultiplier, int numShares) {
        this.id = COUNTER++;
        this.name = name;
        this.sharePrice = sharePrice;
        this.shareMultiplier = shareMultiplier;
        this.numShares = numShares;
    }

    public Company() {
        this.id = COUNTER++;
        this.sharePrice = 30.0;
        this.shareMultiplier = 1.0;
        this.numShares = 1000;
    }
    
    public int getId() {
        return id;
    }
    
    // Add these new getter methods
    public String getName() {
        return name;
    }
    
    public double getSharePrice() {
        return sharePrice;
    }
    
    public double getShareMultiplier() {
        return shareMultiplier;
    }
    
    public int getNumShares() {
        return numShares;
    }
    
    // Setter methods may also be useful
    public void setName(String name) {
        this.name = name;
    }
    
    public void setSharePrice(double sharePrice) {
        this.sharePrice = sharePrice;
    }
    
    public void setShareMultiplier(double shareMultiplier) {
        this.shareMultiplier = shareMultiplier;
    }
    
    public void setNumShares(int numShares) {
        this.numShares = numShares;
    }

    public void decreaseShares(int quantity) {
        this.numShares -= quantity;
    }

    public void increaseShares(int quantity) {
        this.numShares += quantity;
    }
    
    public void sellNumShares(int clientId, int numShares) {}
    public void reclaimNumShares(int clientId, int numShares) {}
    // method should involve random chance as to whether to increase / decrease share multiplier
    public void updateShareMultiplier() {}
    // uses shareMultiplier and maybe more randomness to determine if share price should increase
    public void updateSharePrice() {}
}