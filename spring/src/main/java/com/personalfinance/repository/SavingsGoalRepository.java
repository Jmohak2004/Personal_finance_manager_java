package com.personalfinance.repository;

import com.personalfinance.model.SavingsGoal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface SavingsGoalRepository extends JpaRepository<SavingsGoal, Long> {
    Optional<SavingsGoal> findFirstByOrderByIdAsc();
}
