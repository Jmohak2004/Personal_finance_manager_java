# Personal Finance Manager - Spring Boot Edition

A modern Spring Boot application for managing personal finances, tracking income and expenses, monitoring savings goals, and alerting users if expenses exceed 50% of income.

## Features
- **RESTful API**: Comprehensive endpoints for transactions, savings goals, and dashboard metrics.
- **Modern Interactive Web UI**: Fully responsive dashboard with real-time calculations, category filters, and expense breakdown visualizer.
- **Dynamic 50% Expense Warning Banner**: Automatically alerts when total expenses cross 50% of total income.
- **H2 Persistent Storage & Console**: Built-in database with H2 Console access for development and inspection.
- **Maven Wrapper Included**: Run immediately using `./mvnw` without installing Maven separately.

---

## Getting Started

### Prerequisites
- Java JDK 17 or higher (`java -version`)

### Running the Application
From the `spring` directory:

```bash
# On macOS / Linux:
./mvnw spring-boot:run

# On Windows:
mvnw.cmd spring-boot:run
```

Once running, access:
- **Web Dashboard**: [http://localhost:8080](http://localhost:8080)
- **H2 Database Console**: [http://localhost:8080/h2-console](http://localhost:8080/h2-console)
  - JDBC URL: `jdbc:h2:file:./data/financedb`
  - User: `sa`
  - Password: *(leave blank)*

---

## REST API Endpoints

### 1. Dashboard
- `GET /api/dashboard/summary`
  - Returns `totalIncome`, `totalExpense`, `netBalance`, `savingsGoal`, `savingsProgress`, `goalProgressPercent`, `expenseRatioPercent`, `expenseAlertActive`, `expenseAlertMessage`, and `categoryBreakdown`.

### 2. Transactions
- `GET /api/transactions` — Get all transactions ordered by date.
- `POST /api/transactions` — Create a new transaction.
  ```json
  {
    "amount": 250.00,
    "description": "Weekly Groceries",
    "category": "Food",
    "date": "2026-10-30"
  }
  ```
- `DELETE /api/transactions/{id}` — Delete a transaction by ID.

### 3. Savings Goal
- `GET /api/savings-goal` — Get current savings goal.
- `POST /api/savings-goal` — Update savings goal.
  ```json
  {
    "targetAmount": 2000.00,
    "title": "Emergency Fund"
  }
  ```

---

## Project Structure
```
spring/
├── pom.xml
├── mvnw / mvnw.cmd
├── src/
│   ├── main/
│   │   ├── java/com/personalfinance/
│   │   │   ├── FinanceManagerApplication.java
│   │   │   ├── config/DataInitializer.java
│   │   │   ├── controller/
│   │   │   │   ├── TransactionController.java
│   │   │   │   ├── SavingsGoalController.java
│   │   │   │   └── DashboardController.java
│   │   │   ├── dto/
│   │   │   │   ├── TransactionRequest.java
│   │   │   │   ├── SavingsGoalRequest.java
│   │   │   │   ├── DashboardSummary.java
│   │   │   │   └── CategorySummary.java
│   │   │   ├── model/
│   │   │   │   ├── Transaction.java
│   │   │   │   └── SavingsGoal.java
│   │   │   ├── repository/
│   │   │   │   ├── TransactionRepository.java
│   │   │   │   └── SavingsGoalRepository.java
│   │   │   └── service/FinanceService.java
│   │   └── resources/
│   │       ├── application.properties
│   │       └── static/
│   │           ├── index.html
│   │           ├── css/style.css
│   │           └── js/app.js
│   └── test/
│       └── java/com/personalfinance/
│           └── FinanceManagerApplicationTests.java
```
