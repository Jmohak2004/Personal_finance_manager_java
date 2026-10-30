package com.personalfinance.repository;

import com.personalfinance.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findAllByOrderByDateDescIdDesc();
    List<Transaction> findByCategoryIgnoreCase(String category);
    List<Transaction> findByTypeIgnoreCase(String type);
}
