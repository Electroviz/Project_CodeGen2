package io.swagger.repository;

import io.swagger.entity.TransactionEntity;
import io.swagger.model.Transaction;
import io.swagger.model.entity.TransactionTest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    //Melle
    @Query(value = "SELECT * FROM transaction t WHERE t.from = ?1 AND t.to = ?2", nativeQuery = true)
    List<Transaction> findTransactionsByFromAndToIban(String fromIban, String toIban);

    @Query(value = "SELECT * FROM transaction WHERE from = ?1 OR to = ?1 AND amount = ?2", nativeQuery = true)
    List<Transaction> findTransactionsByAmount(String iban, Double amount);
}
