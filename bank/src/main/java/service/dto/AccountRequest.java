package service.dto;

public class AccountRequest {
    private Integer clientId;
    private String accountName;
    private Double initialFunds;
    
    // Default constructor
    public AccountRequest() {}
    
    public AccountRequest(Integer clientId, String accountName, Double initialFunds) {
        this.clientId = clientId;
        this.accountName = accountName;
        this.initialFunds = initialFunds;
    }
    
    // Getters and setters
    public Integer getClientId() { return clientId; }
    public void setClientId(Integer clientId) { this.clientId = clientId; }
    public String getAccountName() { return accountName; }
    public void setAccountName(String accountName) { this.accountName = accountName; }
    public Double getInitialFunds() { return initialFunds; }
    public void setInitialFunds(Double initialFunds) { this.initialFunds = initialFunds; }
}