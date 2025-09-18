package service.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import service.core.Company;
import service.core.PublicCompany;
import service.core.TradeResponse;

@RestController
public class CompanyController {
    private final boolean TRADE_SUCCESS = true;
    private final boolean TRADE_FAILURE = false;

    // by default spring uses empty constructor for controllers
    // with no dependencies here for clarity
    public CompanyController() {
    }

    /**
     * GET endpoint to check if service is running
     */
    @GetMapping(path="/")
    public ResponseEntity<String> testEndpoint() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body("Company Service is running");
    }

    /**
     * Buy shares from a company
     * @param companyId - ID of company selling shares
     * @param request - contains clientId and quantity
     * @return - TradeResponse with trade details
     */
    @PostMapping(path="/companies/{companyId}/buy", consumes="application/json")
    public ResponseEntity<TradeResponse> buyShares(@PathVariable Integer companyId, 
                                                 @RequestBody ShareRequest request) {
        try {
            // In a real implementation, you would look up the company by ID
            // For now, we'll create a dummy company
            Company company = createDummyCompany(companyId);
            
            // Check if company has enough shares
            if (company.getNumShares() < request.getQuantity()) {
                return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new TradeResponse(TRADE_FAILURE, 0, 0.0));
            }
            
            double tradeValue = request.getQuantity() * company.getSharePrice();
            company.decreaseShares(request.getQuantity());
            
            TradeResponse tradeData = new TradeResponse(TRADE_SUCCESS, request.getQuantity(), tradeValue);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(tradeData);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new TradeResponse(TRADE_FAILURE, 0, 0.0));
        }
    }

    /**
     * Sell shares back to a company
     * @param companyId - ID of company reclaiming shares
     * @param request - contains clientId and quantity
     * @return - TradeResponse with trade details
     */
    @PostMapping(path="/companies/{companyId}/sell", consumes="application/json")
    public ResponseEntity<TradeResponse> sellShares(@PathVariable Integer companyId, 
                                                 @RequestBody ShareRequest request) {
        try {
            // In a real implementation, you would look up the company by ID
            // For now, we'll create a dummy company
            Company company = createDummyCompany(companyId);
            
            // calculate trade value before updating shares
            double tradeValue = request.getQuantity() * company.getSharePrice();
            company.increaseShares(request.getQuantity());
            
            TradeResponse tradeData = new TradeResponse(TRADE_SUCCESS, request.getQuantity(), tradeValue);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(tradeData);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new TradeResponse(TRADE_FAILURE, 0, 0.0));
        }
    }
    
    /**
     * Get company details
     * @param companyId - ID of company to retrieve
     * @return - Company details
     */
    @GetMapping(path="/companies/{companyId}")
    public ResponseEntity<Company> getCompany(@PathVariable Integer companyId) {
        try {
            Company company = createDummyCompany(companyId);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(company);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .build();
        }
    }
    
    // Helper method to create a dummy company for testing
    private Company createDummyCompany(Integer companyId) {
        // In a real implementation, you would look up the company in a database
        // For now, just create a dummy company with the requested ID
        PublicCompany company = new PublicCompany(
            "Tech Corp " + companyId,  // Name with ID to make it identifiable
            100.0,                    // Share price
            1.2,                      // Share multiplier
            1000                      // Number of shares
        );
        
        // We'd normally set this from the database, but for testing we'll use reflection
        try {
            java.lang.reflect.Field idField = Company.class.getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(company, companyId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return company;
    }
}

// Request model class
class ShareRequest {
    private Integer clientId;
    private Integer quantity;
    
    // Default constructor for Jackson
    public ShareRequest() {}
    
    public ShareRequest(Integer clientId, Integer quantity) {
        this.clientId = clientId;
        this.quantity = quantity;
    }
    
    // Getters and setters
    public Integer getClientId() { return clientId; }
    public void setClientId(Integer clientId) { this.clientId = clientId; }
    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
}