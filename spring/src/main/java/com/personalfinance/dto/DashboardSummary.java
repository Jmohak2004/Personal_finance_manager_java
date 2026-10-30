package com.personalfinance.dto;

import java.util.List;

public class DashboardSummary {
    private Double totalIncome;
    private Double totalExpense;
    private Double netBalance;
    private Double savingsGoal;
    private Double savingsProgress;
    private Double goalProgressPercent;
    private Double expenseRatioPercent;
    private Boolean expenseAlertActive;
    private String expenseAlertMessage;
    private List<CategorySummary> categoryBreakdown;
    private int transactionCount;

    public DashboardSummary() {}

    public Double getTotalIncome() {
        return totalIncome;
    }

    public void setTotalIncome(Double totalIncome) {
        this.totalIncome = totalIncome;
    }

    public Double getTotalExpense() {
        return totalExpense;
    }

    public void setTotalExpense(Double totalExpense) {
        this.totalExpense = totalExpense;
    }

    public Double getNetBalance() {
        return netBalance;
    }

    public void setNetBalance(Double netBalance) {
        this.netBalance = netBalance;
    }

    public Double getSavingsGoal() {
        return savingsGoal;
    }

    public void setSavingsGoal(Double savingsGoal) {
        this.savingsGoal = savingsGoal;
    }

    public Double getSavingsProgress() {
        return savingsProgress;
    }

    public void setSavingsProgress(Double savingsProgress) {
        this.savingsProgress = savingsProgress;
    }

    public Double getGoalProgressPercent() {
        return goalProgressPercent;
    }

    public void setGoalProgressPercent(Double goalProgressPercent) {
        this.goalProgressPercent = goalProgressPercent;
    }

    public Double getExpenseRatioPercent() {
        return expenseRatioPercent;
    }

    public void setExpenseRatioPercent(Double expenseRatioPercent) {
        this.expenseRatioPercent = expenseRatioPercent;
    }

    public Boolean getExpenseAlertActive() {
        return expenseAlertActive;
    }

    public void setExpenseAlertActive(Boolean expenseAlertActive) {
        this.expenseAlertActive = expenseAlertActive;
    }

    public String getExpenseAlertMessage() {
        return expenseAlertMessage;
    }

    public void setExpenseAlertMessage(String expenseAlertMessage) {
        this.expenseAlertMessage = expenseAlertMessage;
    }

    public List<CategorySummary> getCategoryBreakdown() {
        return categoryBreakdown;
    }

    public void setCategoryBreakdown(List<CategorySummary> categoryBreakdown) {
        this.categoryBreakdown = categoryBreakdown;
    }

    public int getTransactionCount() {
        return transactionCount;
    }

    public void setTransactionCount(int transactionCount) {
        this.transactionCount = transactionCount;
    }
}
