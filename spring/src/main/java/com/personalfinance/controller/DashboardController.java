package com.personalfinance.controller;

import com.personalfinance.dto.DashboardSummary;
import com.personalfinance.service.FinanceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dashboard")
@CrossOrigin(origins = "*")
public class DashboardController {

    private final FinanceService financeService;

    public DashboardController(FinanceService financeService) {
        this.financeService = financeService;
    }

    @GetMapping("/summary")
    public ResponseEntity<DashboardSummary> getSummary() {
        return ResponseEntity.ok(financeService.getDashboardSummary());
    }
}
