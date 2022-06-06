package io.swagger.repository;

import io.swagger.entity.TransactionEntity;
import io.swagger.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
}
