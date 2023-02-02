package com.sneddsy.bank.service;

import com.sneddsy.bank.domain.BankAccount;
import com.sneddsy.bank.repository.BankAccountRepository;
import com.sneddsy.bank.util.Luhn;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service class for managing Bank Accounts
 */
@Service
@Transactional
public class BankAccountService {

    private final BankAccountRepository bankAccountRepository;

    public BankAccountService(BankAccountRepository bankAccountRepository) {
        this.bankAccountRepository = bankAccountRepository;
    }

    public Optional<BankAccount> createNewAccount(BankAccount entity) {
        String createdAccountNumber = Luhn.generateCardNumber(entity.getTypeOfAccount().getInn());
        entity.setCardNumber(createdAccountNumber);
        return Optional.of(bankAccountRepository.save(entity));
    }
}
