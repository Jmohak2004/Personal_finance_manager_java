package com.personalfinance.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.time.LocalDate;

@Entity
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Amount is required")
    @Positive(message = "Amount must be greater than zero")
    private Double amount;

    @NotBlank(message = "Description cannot be blank")
    private String description;

    @NotBlank(message = "Category is required")
    private String category; // e.g. Income, Food, Rent, Utilities, Entertainment

    private String type; // INCOME or EXPENSE

    private LocalDate date;

    public Transaction() {
        this.date = LocalDate.now();
    }

    public Transaction(Double amount, String description, String category, String type, LocalDate date) {
        this.amount = amount;
        this.description = description;
        this.category = category;
        this.type = type != null ? type : ("Income".equalsIgnoreCase(category) ? "INCOME" : "EXPENSE");
        this.date = date != null ? date : LocalDate.now();
    }

    @PrePersist
    @PreUpdate
    public void determineType() {
        if (this.category != null && "Income".equalsIgnoreCase(this.category.trim())) {
            this.type = "INCOME";
        } else {
            this.type = "EXPENSE";
        }
        if (this.date == null) {
            this.date = LocalDate.now();
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
