package io.swagger.repository;

import io.swagger.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRespository extends JpaRepository<Transaction, Integer> {
}
