package service.dto;

public class PublicCompanyRequest {
    private String name;
    private double sharePrice;
    private double shareMultiplier;
    private int numShares;
    
    // Default constructor
    public PublicCompanyRequest() {}
    
    // Constructor
    public PublicCompanyRequest(String name, double sharePrice, double shareMultiplier, int numShares) {
        this.name = name;
        this.sharePrice = sharePrice;
        this.shareMultiplier = shareMultiplier;
        this.numShares = numShares;
    }
    
    // Getters and setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public double getSharePrice() { return sharePrice; }
    public void setSharePrice(double sharePrice) { this.sharePrice = sharePrice; }
    public double getShareMultiplier() { return shareMultiplier; }
    public void setShareMultiplier(double shareMultiplier) { this.shareMultiplier = shareMultiplier; }
    public int getNumShares() { return numShares; }
    public void setNumShares(int numShares) { this.numShares = numShares; }
}