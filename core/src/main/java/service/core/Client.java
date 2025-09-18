package service.core;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Client implements Serializable {
    private static int COUNTER = 1000;
    private int id;
    private String name;
    private BankAccount bankAccount;
    private List<ShareHolding> portfolio;
    private double totalAssets;

    public Client(String name, BankAccount bankAccount, List<ShareHolding> portfolio) {
        this.id = COUNTER++;
        this.name = name;
        this.bankAccount = bankAccount;
        this.portfolio = portfolio != null ? portfolio : new ArrayList<>();
        updateTotalAssets();
    }
    
    public Client(int id, String name, BankAccount bankAccount) {
        this.id = id;
        this.name = name;
        this.bankAccount = bankAccount;
        this.portfolio = new ArrayList<>();
        updateTotalAssets();
    }

    public Client() {
        this.id = COUNTER++;
        this.portfolio = new ArrayList<>();
    }
    
    // Getters and setters
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public BankAccount getBankAccount() {
        return bankAccount;
    }
    
    public void setBankAccount(BankAccount bankAccount) {
        this.bankAccount = bankAccount;
        updateTotalAssets();
    }
    
    public List<ShareHolding> getPortfolio() {
        return portfolio;
    }
    
    public void setPortfolio(List<ShareHolding> portfolio) {
        this.portfolio = portfolio;
        updateTotalAssets();
    }
    
    public double getTotalAssets() {
        return totalAssets;
    }
    
    // Portfolio management methods
    public void addShareHolding(ShareHolding shareHolding) {
        // Check if player already has shares of this company
        for (ShareHolding holding : portfolio) {
            if (holding.getCompanyId() == shareHolding.getCompanyId()) {
                // Update existing holding
                holding.increaseQuantity(shareHolding.getQuantity());
                holding.addValue(shareHolding.getValue());
                updateTotalAssets();
                return;
            }
        }
        
        // Add new holding
        portfolio.add(shareHolding);
        updateTotalAssets();
    }
    
    public void removeShares(int companyId, int quantity) {
        for (ShareHolding holding : portfolio) {
            if (holding.getCompanyId() == companyId) {
                holding.decreaseQuantity(quantity);
                // If no shares left, remove the holding
                if (holding.getQuantity() <= 0) {
                    portfolio.remove(holding);
                }
                updateTotalAssets();
                return;
            }
        }
    }
    
    public int getShareQuantity(int companyId) {
        for (ShareHolding holding : portfolio) {
            if (holding.getCompanyId() == companyId) {
                return holding.getQuantity();
            }
        }
        return 0;
    }
    
    public double getPortfolioValue() {
        double value = 0.0;
        for (ShareHolding holding : portfolio) {
            value += holding.getValue();
        }
        return value;
    }
    
    public void updateTotalAssets() {
        this.totalAssets = bankAccount.balance + getPortfolioValue();
    }
}