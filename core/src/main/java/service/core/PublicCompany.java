package service.core;

public class PublicCompany extends Company {
    public PublicCompany(String name, double sharePrice, double shareMultiplier, int numShares) {
        super(name, sharePrice, shareMultiplier, numShares);
    }
    
    public PublicCompany() {
        super();
    }
    
    @Override
    public void sellNumShares(int clientId, int numShares) {
        // Implementation for selling shares
        setNumShares(getNumShares() - numShares);
    }
    
    @Override
    public void reclaimNumShares(int clientId, int numShares) {
        // Implementation for reclaiming shares
        setNumShares(getNumShares() + numShares);
    }
    
    @Override
    public void updateShareMultiplier() {
        // Simple implementation - random chance to increase/decrease
        double change = Math.random() > 0.5 ? 0.1 : -0.1;
        setShareMultiplier(getShareMultiplier() + change);
    }
    
    @Override
    public void updateSharePrice() {
        // Update share price based on multiplier
        setSharePrice(getSharePrice() * getShareMultiplier());
    }
}