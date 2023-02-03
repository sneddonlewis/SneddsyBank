package com.sneddsy.bank.service;

import com.sneddsy.bank.domain.BankAccount;
import com.sneddsy.bank.domain.BankTransfer;
import com.sneddsy.bank.repository.BankAccountRepository;
import com.sneddsy.bank.repository.BankTransferRepository;
import com.sneddsy.bank.util.Luhn;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service class for managing Bank Accounts
 */
@Service
@Transactional
public class BankAccountService {

    private final BankAccountRepository bankAccountRepository;
    private final BankTransferRepository bankTransferRepository;

    public BankAccountService(BankAccountRepository bankAccountRepository, BankTransferRepository bankTransferRepository) {
        this.bankAccountRepository = bankAccountRepository;
        this.bankTransferRepository = bankTransferRepository;
    }

    public Optional<BankAccount> createNewAccount(BankAccount entity) {
        String createdAccountNumber = Luhn.generateCardNumber(entity.getTypeOfAccount().getInn());
        entity.setCardNumber(createdAccountNumber);
        return Optional.of(bankAccountRepository.save(entity));
    }

    public List<BankTransfer> getTransfersForAccount(Long accountId) {
        BankAccount account = bankAccountRepository.getReferenceById(accountId);
        return bankTransferRepository.findAll().stream().filter(t -> t.getFromAccount().getId() != accountId).collect(Collectors.toList());
    }
}
