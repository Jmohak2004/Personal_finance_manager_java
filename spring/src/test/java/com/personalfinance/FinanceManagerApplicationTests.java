package com.personalfinance;

import com.personalfinance.dto.DashboardSummary;
import com.personalfinance.dto.SavingsGoalRequest;
import com.personalfinance.dto.TransactionRequest;
import com.personalfinance.model.SavingsGoal;
import com.personalfinance.model.Transaction;
import com.personalfinance.service.FinanceService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class FinanceManagerApplicationTests {

	@Autowired
	private FinanceService financeService;

	@Test
	void contextLoads() {
		assertNotNull(financeService);
	}

	@Test
	void testTransactionAndSummaryCalculation() {
		// Add an income transaction
		TransactionRequest income = new TransactionRequest(2000.0, "Bonus", "Income", LocalDate.now());
		Transaction savedIncome = financeService.addTransaction(income);
		assertNotNull(savedIncome.getId());
		assertEquals("INCOME", savedIncome.getType());

		// Add an expense transaction
		TransactionRequest expense = new TransactionRequest(300.0, "Dinner", "Food", LocalDate.now());
		Transaction savedExpense = financeService.addTransaction(expense);
		assertNotNull(savedExpense.getId());
		assertEquals("EXPENSE", savedExpense.getType());

		// Test Summary
		DashboardSummary summary = financeService.getDashboardSummary();
		assertTrue(summary.getTotalIncome() >= 2000.0);
		assertTrue(summary.getTotalExpense() >= 300.0);
	}

	@Test
	void testSavingsGoal() {
		SavingsGoalRequest req = new SavingsGoalRequest(5000.0, "New Car Fund");
		SavingsGoal goal = financeService.setSavingsGoal(req);
		assertEquals(5000.0, goal.getTargetAmount());

		SavingsGoal retrieved = financeService.getSavingsGoal();
		assertEquals(5000.0, retrieved.getTargetAmount());
	}
}
