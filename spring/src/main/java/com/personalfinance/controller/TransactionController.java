package com.personalfinance.controller;

import com.personalfinance.dto.TransactionRequest;
import com.personalfinance.model.Transaction;
import com.personalfinance.service.FinanceService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@CrossOrigin(origins = "*")
public class TransactionController {

    private final FinanceService financeService;

    public TransactionController(FinanceService financeService) {
        this.financeService = financeService;
    }

    @GetMapping
    public ResponseEntity<List<Transaction>> getAllTransactions() {
        return ResponseEntity.ok(financeService.getAllTransactions());
    }

    @PostMapping
    public ResponseEntity<Transaction> addTransaction(@Valid @RequestBody TransactionRequest request) {
        Transaction created = financeService.addTransaction(request);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransaction(@PathVariable Long id) {
        financeService.deleteTransaction(id);
        return ResponseEntity.noContent().build();
    }
}
