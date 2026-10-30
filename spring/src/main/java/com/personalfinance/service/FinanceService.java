package com.personalfinance.service;

import com.personalfinance.dto.CategorySummary;
import com.personalfinance.dto.DashboardSummary;
import com.personalfinance.dto.SavingsGoalRequest;
import com.personalfinance.dto.TransactionRequest;
import com.personalfinance.model.SavingsGoal;
import com.personalfinance.model.Transaction;
import com.personalfinance.repository.SavingsGoalRepository;
import com.personalfinance.repository.TransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class FinanceService {

    private final TransactionRepository transactionRepository;
    private final SavingsGoalRepository savingsGoalRepository;

    public FinanceService(TransactionRepository transactionRepository, SavingsGoalRepository savingsGoalRepository) {
        this.transactionRepository = transactionRepository;
        this.savingsGoalRepository = savingsGoalRepository;
    }

    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAllByOrderByDateDescIdDesc();
    }

    @Transactional
    public Transaction addTransaction(TransactionRequest request) {
        Transaction transaction = new Transaction();
        transaction.setAmount(request.getAmount());
        transaction.setDescription(request.getDescription());
        transaction.setCategory(request.getCategory());
        transaction.setDate(request.getDate());
        transaction.determineType();
        return transactionRepository.save(transaction);
    }

    @Transactional
    public void deleteTransaction(Long id) {
        transactionRepository.deleteById(id);
    }

    public SavingsGoal getSavingsGoal() {
        return savingsGoalRepository.findFirstByOrderByIdAsc()
                .orElseGet(() -> {
                    SavingsGoal defaultGoal = new SavingsGoal(0.0, "My Savings Goal");
                    return savingsGoalRepository.save(defaultGoal);
                });
    }

    @Transactional
    public SavingsGoal setSavingsGoal(SavingsGoalRequest request) {
        SavingsGoal goal = savingsGoalRepository.findFirstByOrderByIdAsc()
                .orElse(new SavingsGoal());
        goal.setTargetAmount(request.getTargetAmount());
        if (request.getTitle() != null && !request.getTitle().isBlank()) {
            goal.setTitle(request.getTitle());
        }
        return savingsGoalRepository.save(goal);
    }

    public DashboardSummary getDashboardSummary() {
        List<Transaction> transactions = transactionRepository.findAll();
        SavingsGoal goal = getSavingsGoal();

        double totalIncome = 0.0;
        double totalExpense = 0.0;
        Map<String, Double> expenseByCategory = new HashMap<>();

        for (Transaction t : transactions) {
            if ("INCOME".equalsIgnoreCase(t.getType()) || "Income".equalsIgnoreCase(t.getCategory())) {
                totalIncome += t.getAmount();
            } else {
                totalExpense += t.getAmount();
                String category = t.getCategory() != null ? t.getCategory() : "Other";
                expenseByCategory.put(category, expenseByCategory.getOrDefault(category, 0.0) + t.getAmount());
            }
        }

        double netBalance = totalIncome - totalExpense;
        double savingsGoalAmount = goal.getTargetAmount() != null ? goal.getTargetAmount() : 0.0;
        double savingsProgress = netBalance;

        double goalProgressPercent = 0.0;
        if (savingsGoalAmount > 0) {
            goalProgressPercent = Math.max(0.0, Math.min(100.0, (savingsProgress / savingsGoalAmount) * 100.0));
        }

        double expenseRatio = 0.0;
        if (totalIncome > 0) {
            expenseRatio = (totalExpense / totalIncome) * 100.0;
        }

        // Warning logic matches the original Swing application:
        // if (totalIncome > 0 && totalExpense > totalIncome * 0.5)
        boolean alertActive = totalIncome > 0 && totalExpense > (totalIncome * 0.5);
        String alertMessage = alertActive ? "Warning: Your expenses have exceeded 50% of your income!" : null;

        List<CategorySummary> categorySummaries = new ArrayList<>();
        for (Map.Entry<String, Double> entry : expenseByCategory.entrySet()) {
            double pct = totalExpense > 0 ? (entry.getValue() / totalExpense) * 100.0 : 0.0;
            categorySummaries.add(new CategorySummary(
                    entry.getKey(),
                    Math.round(entry.getValue() * 100.0) / 100.0,
                    Math.round(pct * 10.0) / 10.0
            ));
        }
        categorySummaries.sort((a, b) -> Double.compare(b.getTotalAmount(), a.getTotalAmount()));

        DashboardSummary summary = new DashboardSummary();
        summary.setTotalIncome(Math.round(totalIncome * 100.0) / 100.0);
        summary.setTotalExpense(Math.round(totalExpense * 100.0) / 100.0);
        summary.setNetBalance(Math.round(netBalance * 100.0) / 100.0);
        summary.setSavingsGoal(savingsGoalAmount);
        summary.setSavingsProgress(Math.round(savingsProgress * 100.0) / 100.0);
        summary.setGoalProgressPercent(Math.round(goalProgressPercent * 10.0) / 10.0);
        summary.setExpenseRatioPercent(Math.round(expenseRatio * 10.0) / 10.0);
        summary.setExpenseAlertActive(alertActive);
        summary.setExpenseAlertMessage(alertMessage);
        summary.setCategoryBreakdown(categorySummaries);
        summary.setTransactionCount(transactions.size());

        return summary;
    }
}
