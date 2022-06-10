package io.swagger.repository;

import io.swagger.model.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BankAccountRepository extends JpaRepository<BankAccount, Long> {
    List<BankAccount> findByiban(String fromIban);

    List<BankAccount> findByuserId(Long fromUserId);
}
