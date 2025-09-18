package service.bank;
import service.core.*;
import java.util.HashMap;
import org.springframework.stereotype.Service;

@Service
public class BankService {
    private float interestRate = 0.05f; // 5% interest rate
    private int accountNumberCounter = 0; 
    private HashMap<Integer, BankAccount> clientIdToAccounts = new HashMap<Integer, BankAccount>(); // map of clientId to BankAccount	

	public BankAccount createAccount(int clientId, String accountName, double funds) {
        BankAccount account = new BankAccount(accountNumberCounter++, accountName, funds);
        clientIdToAccounts.put(clientId, account);
        return account;
    }

	public void removeAccount(int clientId) {
        clientIdToAccounts.remove(clientId);
    }

    public void depositFunds(int clientId, double amount) {
        BankAccount account = clientIdToAccounts.get(clientId);
        if (account != null) {
            account.balance += amount;
        } else {
            throw new IllegalArgumentException("Account not found");
        }
    }

    public void withdrawFunds(int clientId, double amount) {
        BankAccount account = clientIdToAccounts.get(clientId);
        if (account != null && account.balance >= amount) {
            account.balance -= amount;
        } else if (account != null && account.balance < amount){
            throw new IllegalArgumentException("Request denied due to insufficient funds");
        } else {
            throw new IllegalArgumentException("Account not found");
        }
    }

    public double getBalance(int clientId) {
        BankAccount account = clientIdToAccounts.get(clientId);
        if (account != null) {
            return account.balance;
        } else {
            throw new IllegalArgumentException("Account not found");
        }
    }

    public void updateInterest() {
        // update the interest on all accounts
        for (BankAccount account : clientIdToAccounts.values()) {
            account.balance += account.balance * interestRate;
        }
    }

    public BankAccount getAccount(int clientId) {
        return clientIdToAccounts.get(clientId);
    }

}
