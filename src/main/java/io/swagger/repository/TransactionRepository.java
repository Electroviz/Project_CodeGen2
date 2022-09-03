package io.swagger.repository;

import io.swagger.entity.TransactionEntity;
import io.swagger.model.Transaction;
import io.swagger.model.entity.TransactionTest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    //Melle
    @Query(value = "SELECT * FROM TRANSACTION t WHERE t.from = ?1 AND t.to = ?2", nativeQuery = true)
    Transaction findTransactionByFromAndToIban(String fromIban, String toIban);
}
