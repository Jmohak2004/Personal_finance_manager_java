package com.personalfinance.config;

import com.personalfinance.model.SavingsGoal;
import com.personalfinance.model.Transaction;
import com.personalfinance.repository.SavingsGoalRepository;
import com.personalfinance.repository.TransactionRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initData(TransactionRepository transactionRepository,
                                     SavingsGoalRepository savingsGoalRepository) {
        return args -> {
            if (transactionRepository.count() == 0) {
                // Initialize default savings goal
                savingsGoalRepository.save(new SavingsGoal(1500.0, "Vacation & Emergency Fund"));

                // Sample starter transactions
                transactionRepository.save(new Transaction(3500.0, "Monthly Salary", "Income", "INCOME", LocalDate.now().minusDays(10)));
                transactionRepository.save(new Transaction(1000.0, "Apartment Rent", "Rent", "EXPENSE", LocalDate.now().minusDays(8)));
                transactionRepository.save(new Transaction(250.0, "Weekly Groceries", "Food", "EXPENSE", LocalDate.now().minusDays(5)));
                transactionRepository.save(new Transaction(120.0, "Electricity & Water Bill", "Utilities", "EXPENSE", LocalDate.now().minusDays(3)));
                transactionRepository.save(new Transaction(80.0, "Movie & Dining Out", "Entertainment", "EXPENSE", LocalDate.now().minusDays(1)));
            }
        };
    }
}
