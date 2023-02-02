package com.sneddsy.bank.service;

import com.sneddsy.bank.domain.BankAccount;
import com.sneddsy.bank.domain.BankTransfer;
import com.sneddsy.bank.repository.BankAccountRepository;
import com.sneddsy.bank.repository.BankTransferRepository;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service class for handling payments and transfers between bank accounts
 */
@Service
@Transactional
public class PaymentService {

    private final BankAccountRepository bankAccountRepository;
    private final BankTransferRepository bankTransferRepository;

    public PaymentService(BankAccountRepository bankAccountRepository, BankTransferRepository bankTransferRepository) {
        this.bankAccountRepository = bankAccountRepository;
        this.bankTransferRepository = bankTransferRepository;
    }

    @Transactional
    public Optional<BankTransfer> makeTransfer(BankTransfer request) {
        BigDecimal amount = request.getAmount();
        Long fromId = request.getFromAccount().getId();
        Long toId = request.getToAccount().getId();

        BankAccount instigator = bankAccountRepository.getReferenceById(fromId);
        if (instigator.getBalance().compareTo(amount) < 0) {
            // not enough money
            return Optional.empty();
        }
        BankAccount transferee = bankAccountRepository.getReferenceById(toId); // handle doesn't exist
        BankTransfer transfer = new BankTransfer()
            .amount(amount)
            .fromAccount(instigator)
            .toAccount(transferee)
            .executionTime(ZonedDateTime.now());
        instigator.setBalance(instigator.getBalance().subtract(amount));
        transferee.setBalance(transferee.getBalance().add(amount));
        bankAccountRepository.save(instigator);
        bankAccountRepository.save(transferee);
        return Optional.of(bankTransferRepository.save(transfer));
    }
}
