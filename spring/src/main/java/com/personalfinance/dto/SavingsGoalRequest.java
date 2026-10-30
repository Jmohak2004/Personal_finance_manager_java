package com.personalfinance.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public class SavingsGoalRequest {

    @NotNull(message = "Savings goal amount is required")
    @PositiveOrZero(message = "Savings goal cannot be negative")
    private Double targetAmount;

    private String title;

    public SavingsGoalRequest() {}

    public SavingsGoalRequest(Double targetAmount, String title) {
        this.targetAmount = targetAmount;
        this.title = title;
    }

    public Double getTargetAmount() {
        return targetAmount;
    }

    public void setTargetAmount(Double targetAmount) {
        this.targetAmount = targetAmount;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
