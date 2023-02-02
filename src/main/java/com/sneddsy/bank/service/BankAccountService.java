package com.sneddsy.bank.service;

import com.sneddsy.bank.domain.BankAccount;
import com.sneddsy.bank.repository.BankAccountRepository;
import com.sneddsy.bank.util.Luhn;
import java.util.Arrays;
import java.util.Optional;
import java.util.Random;
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

    public BankAccountService(BankAccountRepository bankAccountRepository) {
        this.bankAccountRepository = bankAccountRepository;
    }

    public Optional<BankAccount> createNewAccount(BankAccount entity) {
        entity.setCardNumber(generateCardNumber("400000"));
        return Optional.of(bankAccountRepository.save(entity));
    }

    private static String generateCardNumber(String INN) {
        int[] digits = new Random().ints(9, 0, 9).toArray();
        int checkDigit = Luhn.getCheckSum(INN, digits);
        return INN + Arrays.stream(digits).mapToObj(String::valueOf).collect(Collectors.joining()) + checkDigit;
    }
}
