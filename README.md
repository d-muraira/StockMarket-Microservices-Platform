# StockMarket-Microservices-Platform

A distributed stock market simulation system built with microservices architecture

🏗️ **Architecture:** RESTful microservices using Spring Boot  
🐳 **Deployment:** Dockerized services for scalability  
📊 **Features:** Portfolio management, real-time trading, bank account integration  
🔧 **Tech Stack:** Java, Spring Boot, REST APIs, Docker, OpenAPI  

## 📋 Table of Contents
- [Overview](#overview)
- [System Architecture](#system-architecture)
- [Services](#services)
- [Key Features](#key-features)
- [Getting Started](#getting-started)
- [API Documentation](#api-documentation)
- [Contributors](#contributors)

## 🌟 Overview

This project simulates a stock market on a small scale, allowing players to use funds in a bank account to buy and sell stocks from multiple companies. The system is built using a microservices architecture where each component operates independently while communicating through REST APIs.

## 🏛️ System Architecture

```
┌─────────────┐    ┌─────────────┐    ┌─────────────┐
│   Player    │───▶│   Market    │───▶│    Bank     │
│  Service    │    │  Service    │    │  Service    │
└─────────────┘    └─────────────┘    └─────────────┘
                           │
                           ▼
                   ┌─────────────┐
                   │  Company    │
                   │  Services   │
                   └─────────────┘
```

The Market Service acts as the central broker, facilitating all transactions between players, banks, and companies while maintaining a comprehensive transaction ledger.

## 🚀 Services

### Player Service
- **Purpose:** Portfolio management and trading interface
- **Key Endpoints:**
  - `GET /players` - Get all players
  - `POST /players` - Create new player
  - `GET /players/{playerId}/portfolio` - Get player's portfolio
  - `POST /players/{playerId}/buy` - Execute buy order
  - `POST /players/{playerId}/sell` - Execute sell order

### Market Service
- **Purpose:** Central broker handling all transactions
- **Key Endpoints:**
  - `GET /companies` - List all available companies
  - `POST /companies/{companyId}/buy` - Process buy orders
  - `POST /companies/{companyId}/sell` - Process sell orders
  - `GET /trades` - View transaction history

### Bank Service
- **Purpose:** Account management and fund transfers
- **Key Endpoints:**
  - `POST /accounts` - Create new bank account
  - `GET /accounts/{accountId}/balance` - Check account balance
  - `POST /accounts/{accountId}/deposit` - Deposit funds
  - `POST /accounts/{accountId}/withdraw` - Withdraw funds

### Company Services
- **Purpose:** Individual stock price management and company data
- **Key Endpoints:**
  - `POST /company/buy` - Handle company stock purchases
  - `POST /company/sell` - Handle company stock sales

## ✨ Key Features

- 📈 **Real-time Trading:** Buy and sell stock transactions with immediate portfolio updates
- 💰 **Bank Integration:** Seamless account management with interest calculations
- 📊 **Portfolio Management:** Track holdings, values, and total assets
- 🔄 **Transaction Ledger:** Complete trade history with rollback capabilities
- 🛡️ **Error Handling:** Comprehensive fault tolerance and meaningful error messages
- 📚 **API Documentation:** OpenAPI specifications for all services
- 🐳 **Containerization:** Docker support for easy deployment and scaling
- 🔗 **Service Communication:** Abstracted inter-service communication through client classes

## 🚀 Getting Started

### Prerequisites
- Java 17+
- Docker and Docker Compose
- Maven

### Installation

1. **Clone the repository**
   ```bash
   git clone https://github.com/yourusername/StockMarket-Microservices-Platform.git
   cd StockMarket-Microservices-Platform
   ```

2. **Build the services**
   ```bash
   mvn clean install
   ```

3. **Run with Docker Compose**
   ```bash
   docker-compose up --build
   ```

4. **Verify services are running**
   - Player Service: http://localhost:8080
   - Market Service: http://localhost:8081
   - Bank Service: http://localhost:8082
   - Company Service: http://localhost:8083

### Quick Start Guide

1. **Create a player account**
   ```bash
   curl -X POST http://localhost:8080/players \
     -H "Content-Type: application/json" \
     -d '{"name": "John Doe"}'
   ```

2. **Check available companies**
   ```bash
   curl http://localhost:8080/market/companies
   ```

3. **Buy some stocks**
   ```bash
   curl -X POST http://localhost:8080/players/1/buy \
     -H "Content-Type: application/json" \
     -d '{"companyId": 1, "quantity": 10}'
   ```

## 📖 API Documentation

Each service includes OpenAPI documentation accessible at:
- Player Service: http://localhost:8080/swagger-ui.html
- Market Service: http://localhost:8081/swagger-ui.html
- Bank Service: http://localhost:8082/swagger-ui.html
- Company Service: http://localhost:8083/swagger-ui.html

## 🛠️ Design Patterns

- **Microservices Architecture:** Independent, loosely coupled services
- **Client Pattern:** Abstracted inter-service communication
- **Repository Pattern:** Data access layer abstraction
- **DTO Pattern:** Data transfer between services
- **Global Exception Handling:** Centralized error management

## 🎯 Future Enhancements

- Real-time stock price fluctuations
- Multiple player competition mode
- Advanced portfolio analytics
- Mobile application interface
- Integration with external financial APIs

## 👥 Contributors

- **Ethan Epperson** - Core module, Market service, Company service
- **Alex Waldman** - Bank service, Documentation, System architecture
- **David Martinez** - Player service, Docker containerization, Service integration

## 📄 License

This project was developed as part of COMP30220 Distributed Systems coursework at University College Dublin (UCD).

---
