package service.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/test")
public class TestController {

    @GetMapping
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("Test endpoint working");
    }
    
    @PostMapping("/company")
    public ResponseEntity<CompanyTestDTO> createCompany(@RequestBody CompanyTestDTO company) {
        // Simply echo back the received company
        return ResponseEntity.ok(company);
    }
}

// Simple DTO class to avoid depending on core module
class CompanyTestDTO {
    private String name;
    private double sharePrice;
    
    // Default constructor needed for Jackson
    public CompanyTestDTO() {}
    
    public CompanyTestDTO(String name, double sharePrice) {
        this.name = name;
        this.sharePrice = sharePrice;
    }
    
    // Getters and setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public double getSharePrice() { return sharePrice; }
    public void setSharePrice(double sharePrice) { this.sharePrice = sharePrice; }
}