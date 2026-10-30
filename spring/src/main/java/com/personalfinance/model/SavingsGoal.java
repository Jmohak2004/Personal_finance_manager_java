package com.personalfinance.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;

@Entity
@Table(name = "savings_goal")
public class SavingsGoal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Target amount is required")
    @PositiveOrZero(message = "Target amount cannot be negative")
    private Double targetAmount;

    private String title;

    private LocalDateTime updatedAt;

    public SavingsGoal() {
        this.targetAmount = 0.0;
        this.title = "Personal Savings Goal";
        this.updatedAt = LocalDateTime.now();
    }

    public SavingsGoal(Double targetAmount, String title) {
        this.targetAmount = targetAmount;
        this.title = title != null ? title : "Personal Savings Goal";
        this.updatedAt = LocalDateTime.now();
    }

    @PrePersist
    @PreUpdate
    public void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
