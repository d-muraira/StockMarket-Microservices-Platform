package service.controllers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import service.bank.BankService;
import service.core.BankAccount;
import service.dto.AccountRequest;
import service.dto.AmountRequest;

/** Banking Service
 *
 * Endpoints:
 * - POST /accounts - create a new bank account
 * - GET /accounts/{clientId} - get a specific bank account
 * - DELETE /accounts/{clientId} - remove a bank account
 * - POST /accounts/{clientId}/deposit - deposit funds into a bank account
 * - POST /accounts/{clientId}/withdraw - withdraw funds from a bank account
 * - GET /accounts/{clientId}/balance - get the balance of a bank account
 *
 */

@RestController
public class BankController {
    private final BankService bankService;
    
    @Autowired
    public BankController(BankService bank) {
        this.bankService = bank;
    }

    @GetMapping(path="/", produces="application/json")
    public ResponseEntity<Map<String, String>> testEndpoint() {
    Map<String, String> response = new HashMap<>();
    response.put("message", "Bank Service is running");
    return ResponseEntity
            .status(HttpStatus.OK)
            .body(response);
    }
    
    // Create a new bank account
    @PostMapping(path="/accounts", consumes="application/json", produces="application/json")
    public ResponseEntity<BankAccount> createAccount(@RequestBody AccountRequest request) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(bankService.createAccount(request.getClientId(), request.getAccountName(), request.getInitialFunds()));
    }

    // Remove a bank account
    @DeleteMapping(path="/accounts/{clientId}")
    public ResponseEntity<Void> removeAccount(@PathVariable Integer clientId) {
        bankService.removeAccount(clientId);
        return ResponseEntity
                .noContent()
                .build();
    }

    // Get a specific bank account
    @GetMapping(path="/accounts/{clientId}", produces="application/json")
    public ResponseEntity<BankAccount> getAccount(@PathVariable Integer clientId) {
        BankAccount account = bankService.getAccount(clientId);
        if (account != null) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(account);
        } else {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .build();
        }
    }

    // Deposit funds into a bank account
    @PostMapping(path="/accounts/{clientId}/deposit", produces="application/json")
    public ResponseEntity<Void> depositFunds(@PathVariable Integer clientId, @RequestBody AmountRequest request) {
        BankAccount account = bankService.getAccount(clientId);
        if (account != null) {
            bankService.depositFunds(clientId, request.getAmount());
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .build();
        } else {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .build();
        }
    }
    
    // Withdraw funds from a bank account
    @PostMapping(path="/accounts/{clientId}/withdraw", produces="application/json")
    public ResponseEntity<Void> withdrawFunds(@PathVariable Integer clientId, @RequestBody AmountRequest request) {
        BankAccount account = bankService.getAccount(clientId);
        if (account != null) {
            try {
                bankService.withdrawFunds(clientId, request.getAmount());
                return ResponseEntity
                    .status(HttpStatus.OK)
                    .build();
            } catch (IllegalArgumentException e) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .build();
            }
        } else {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .build();
        }
    }
    
    // Get the balance of a bank account
    @GetMapping(path="/accounts/{clientId}/balance", produces="application/json")
    public ResponseEntity<Double> getBalance(@PathVariable Integer clientId) {
        BankAccount account = bankService.getAccount(clientId);
        if (account != null) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(account.balance);
        } else {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .build();
        }
    }
}