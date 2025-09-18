package service.core;

import java.io.Serializable;

public class BankAccount implements Serializable {
    public int number;
    public String name;
    public double balance;

    public BankAccount(int number, String name, double balance) {
        this.number = number;
        this.name = name;
        this.balance = balance;
    }

    public BankAccount() {}
    
    // Getters and setters
    public int getNumber() {
        return number;
    }
    
    public void setNumber(int number) {
        this.number = number;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public double getBalance() {
        return balance;
    }
    
    public void setBalance(double balance) {
        this.balance = balance;
    }
}