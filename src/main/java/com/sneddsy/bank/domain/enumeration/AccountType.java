package com.sneddsy.bank.domain.enumeration;

/**
 * The AccountType enumeration.
 */
public enum AccountType {
    CURRENT("400000"),
    SAVING("500000"),
    LOAN("600000");

    private final String inn;

    AccountType(String inn) {
        this.inn = inn;
    }

    public String getInn() {
        return inn;
    }
}
