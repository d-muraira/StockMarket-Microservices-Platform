package service.market;

import org.springframework.stereotype.Service;
import service.core.Company;
import service.core.Trade;
import service.market.exception.CompanyNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/** MarketService
 * Purpose:
 * - Manage which companies are in the market
 * - MarketController utilizes a MarketService to manage companies
 */

// Service annotates classes that handle business logic
// makes MarketService a bean for Spring to manage
@Service
public class MarketService {
    private Map<Integer, Company> companies;
    private Map<Integer, List<Trade>> trades;

    public MarketService(Map<Integer, Company> companies, Map<Integer, List<Trade>> trades) {
        this.companies = companies;
        this.trades = trades;
    }

    public MarketService() {
        this.companies = new ConcurrentHashMap<>();
        this.trades = new ConcurrentHashMap<>();
    }

    public void addCompany(Company company) {
        try {
            // Validate company
            if (company == null) {
                throw new IllegalArgumentException("Company cannot be null");
            }
            
            int companyId = company.getId();
            
            // Log for debugging
            System.out.println("Adding company: " + company.getClass().getName() + 
                               ", ID: " + companyId + 
                               ", Name: " + company.getName());
            
            // Add to companies map
            companies.putIfAbsent(companyId, company);
            
            System.out.println("Company added successfully");
        } catch (Exception e) {
            // Log the exception
            System.err.println("Error adding company: " + e.getMessage());
            e.printStackTrace();
            throw e; // Re-throw to let Spring handle it
        }
    }

    public void removeCompany(int companyId) {
        if (!companies.containsKey(companyId)) {
            throw new CompanyNotFoundException("Company with ID " + companyId + " not found");
        }
        companies.remove(companyId);
    }

    public List<Company> getCompanies() {
        return new ArrayList<>(companies.values());
    }

    public Company getCompany(int companyId) {
        Company company = companies.get(companyId);
        if (company == null) {
            throw new CompanyNotFoundException("Company with ID " + companyId + " not found");
        }
        return company;
    }

    public void addTrade(Trade trade) {
        try {
            if (trade == null) {
                throw new IllegalArgumentException("Trade cannot be null");
            }
            
            int tradeId = trade.getId();
            
            // Log for debugging
            System.out.println("Adding trade: ID: " + tradeId + 
                               ", Company: " + trade.getCompanyName() + 
                               ", Quantity: " + trade.getQuantityPurchased());
            
            // computeIfAbsent returns the list if it exists already, otherwise creates new
            trades.computeIfAbsent(tradeId, k -> new ArrayList<>()).add(trade);
            
            System.out.println("Trade added successfully");
        } catch (Exception e) {
            System.err.println("Error adding trade: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    public List<Trade> getTrades(Integer companyId) {
        List<Trade> companyTrades = trades.get(companyId);
        if (companyTrades == null) {
            return new ArrayList<>(); // Return empty list instead of null
        }
        return companyTrades;
    }

    public List<Trade> getAllTrades() {
        List<Trade> allTrades = new ArrayList<>();
        for (List<Trade> tradeList : trades.values()) {
            allTrades.addAll(tradeList);
        }
        return allTrades;
    }
}