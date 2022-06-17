package io.swagger.repository;

import io.swagger.entity.TransactionEntity;
import io.swagger.model.Transaction;
import io.swagger.model.entity.TransactionTest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<TransactionTest, Long> {
}
