package service.controllers;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import service.clients.BankClient;
import service.clients.CompanyClient;
import service.core.Company;
import service.core.CompanyDTO;
import service.core.PublicCompany;
import service.core.Trade;
import service.dto.BuyRequest;
import service.dto.SellRequest;
import service.dto.PublicCompanyRequest;
import service.market.MarketService;
import service.market.exception.CompanyNotFoundException;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class MarketController {
    private final MarketService market;
    private final BankClient bankClient;
    private final CompanyClient companyClient;

    @Autowired
    public MarketController(MarketService market, BankClient bankClient, CompanyClient companyClient) {
        this.market = market;
        this.bankClient = bankClient;
        this.companyClient = companyClient;
    }

    @Value("${server.port:8082}")
    private int serverPort;

    @GetMapping(path="/")
    public ResponseEntity<String> testEndpoint() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body("Market Service is running");
    }

    @GetMapping(path="/companies", produces="application/json")
    public ResponseEntity<List<Company>> getAllCompanies() {
        try {
            List<Company> companies = market.getCompanies();
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .header("X-Has-Polymorphic-Types", "true")
                    .body(companies);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
    
    @GetMapping(path="/company-dtos", produces="application/json")
    public ResponseEntity<List<CompanyDTO>> getCompanyDTOs() {
        try {
            List<Company> companies = market.getCompanies();
            List<CompanyDTO> companyDTOs = companies.stream()
                .map(company -> new CompanyDTO(
                    company.getId(), 
                    company.getName(), 
                    company.getSharePrice(), 
                    company.getNumShares()))
                .collect(Collectors.toList());
            
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(companyDTOs);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
    
    @GetMapping(path="/company-dtos/{companyId}", produces="application/json")
    public ResponseEntity<CompanyDTO> getCompanyDTO(@PathVariable Integer companyId) {
        try {
            Company company = market.getCompany(companyId);
            CompanyDTO companyDTO = new CompanyDTO(
                company.getId(),
                company.getName(),
                company.getSharePrice(),
                company.getNumShares()
            );
            
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(companyDTO);
        } catch (CompanyNotFoundException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @PostMapping(path="/companies", consumes="application/json")
    public ResponseEntity<Company> addCompanyToMarket(HttpServletRequest request, @RequestBody Company company) {
        try {
            System.out.println("Received company: " + company.getClass().getName());
            market.addCompany(company);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .header("Content-Location", request.getRequestURI())
                    .body(company);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
    
    @PostMapping(path="/public-companies", consumes="application/json")
    public ResponseEntity<PublicCompany> addPublicCompanyToMarket(HttpServletRequest request, 
                                                               @RequestBody PublicCompanyRequest companyRequest) {
        try {
            // Create a new PublicCompany from the request
            PublicCompany company = new PublicCompany(
                companyRequest.getName(),
                companyRequest.getSharePrice(),
                companyRequest.getShareMultiplier(),
                companyRequest.getNumShares()
            );
            
            System.out.println("Received public company: " + company.getName());
            market.addCompany(company);
            
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .header("Content-Location", request.getRequestURI())
                    .body(company);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @GetMapping(path="/companies/{companyId}", produces="application/json")
    public ResponseEntity<Company> getCompany(@PathVariable Integer companyId) {
        try {
            Company company = market.getCompany(companyId);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(company);
        } catch (CompanyNotFoundException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .build();
        }
    }

    @DeleteMapping(path="/companies/{companyId}")
    public ResponseEntity<Void> removeCompanyFromMarket(@PathVariable Integer companyId) {
        try {
            market.removeCompany(companyId);
            return ResponseEntity
                    .noContent()
                    .build();
        } catch (CompanyNotFoundException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .build();
        }
    }

    @PostMapping(path="/companies/{companyId}/buy", consumes="application/json", produces="application/json")
    public ResponseEntity<Trade> buyCompanyShares(@RequestBody BuyRequest request, @PathVariable Integer companyId) {
        try {
            // Verify the company exists
            Company company = market.getCompany(companyId);
            
            Integer clientId = request.getClientId();
            
            // Try to get balance from Bank Service
            Double balance;
            try {
                balance = bankClient.getBalance(clientId);
            } catch (Exception e) {
                // If there's an error getting the balance, return a meaningful error
                return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(null);
            }
            
            Double totalCost = company.getSharePrice() * request.getQuantity();
            
            if (balance < totalCost) {
                return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(null);
            }
            
            // Withdraw funds from bank account
            try {
                bankClient.withdrawFunds(clientId, totalCost);
            } catch (Exception e) {
                // If there's an error withdrawing funds, return a meaningful error
                return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(null);
            }
            
            // Update company shares and purchase shares
            try {
                companyClient.buyShares(companyId, clientId, request.getQuantity());
            } catch (Exception e) {
                // If there's an error with the company client, attempt to refund the money
                try {
                    bankClient.depositFunds(clientId, totalCost);
                } catch (Exception refundError) {
                    // Log the refund error but don't change the response
                    System.err.println("Failed to refund after company client error: " + refundError.getMessage());
                }
                
                return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
            }
            
            // Create trade record
            Trade trade = new Trade(
                clientId,
                companyId,
                company.getName(),
                company.getSharePrice(),
                request.getQuantity()
            );
            
            // Add trade to market history
            market.addTrade(trade);
            
            return ResponseEntity
                .status(HttpStatus.OK)
                .body(trade);
        } catch (CompanyNotFoundException e) {
            return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .build();
        }
    }

    @PostMapping(path="/companies/{companyId}/sell", consumes="application/json", produces="application/json")
    public ResponseEntity<Trade> sellCompanyShares(@RequestBody SellRequest request, @PathVariable Integer companyId) {
        try {
            // Verify the company exists
            Company company = market.getCompany(companyId);
            
            Integer clientId = request.getClientId();
            
            // Calculate funds to deposit
            Double totalValue = company.getSharePrice() * request.getQuantity();
            
            // Deposit funds to bank account
            try {
                bankClient.depositFunds(clientId, totalValue);
            } catch (Exception e) {
                // If there's an error depositing funds, return a meaningful error
                return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(null);
            }
            
            // Update company shares and sell shares
            try {
                companyClient.sellShares(companyId, clientId, request.getQuantity());
            } catch (Exception e) {
                // If there's an error with the company client, attempt to reverse the deposit
                try {
                    bankClient.withdrawFunds(clientId, totalValue);
                } catch (Exception reverseError) {
                    // Log the reverse error but don't change the response
                    System.err.println("Failed to reverse deposit after company client error: " + reverseError.getMessage());
                }
                
                return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
            }
            
            // Create trade record with negative quantity (indicating a sell)
            Trade trade = new Trade(
                clientId,
                companyId,
                company.getName(),
                company.getSharePrice(),
                -request.getQuantity() // Negative to indicate a sell
            );
            
            // Add trade to market history
            market.addTrade(trade);
            
            return ResponseEntity
                .status(HttpStatus.OK)
                .body(trade);
        } catch (CompanyNotFoundException e) {
            return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .build();
        }
    }

    @GetMapping(path="/trades", produces="application/json")
    public ResponseEntity<List<Trade>> getTradeHistory(@RequestParam(required = false) Integer companyId) {
        try {
            // fetch trade history of all companies
            if (companyId == null) {
                return ResponseEntity
                        .status(HttpStatus.OK)
                        .body(market.getAllTrades());
            }
    
            // return trades for individual company
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(market.getTrades(companyId));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .build();
        }
    }

    private String getHost() {
        try {
            InetAddress host = InetAddress.getLocalHost();
            return "%s:%d".formatted(host.getHostAddress(),
                    serverPort);
        } catch (UnknownHostException unknownHostException) {
            unknownHostException.printStackTrace();
        }

        return null;
    }
}