package com.personalfinance.controller;

import com.personalfinance.dto.SavingsGoalRequest;
import com.personalfinance.model.SavingsGoal;
import com.personalfinance.service.FinanceService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/savings-goal")
@CrossOrigin(origins = "*")
public class SavingsGoalController {

    private final FinanceService financeService;

    public SavingsGoalController(FinanceService financeService) {
        this.financeService = financeService;
    }

    @GetMapping
    public ResponseEntity<SavingsGoal> getSavingsGoal() {
        return ResponseEntity.ok(financeService.getSavingsGoal());
    }

    @PostMapping
    public ResponseEntity<SavingsGoal> setSavingsGoal(@Valid @RequestBody SavingsGoalRequest request) {
        return ResponseEntity.ok(financeService.setSavingsGoal(request));
    }
}
