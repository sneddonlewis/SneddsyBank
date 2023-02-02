package com.sneddsy.bank.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A BankAccount.
 */
@Entity
@Table(name = "bank_account")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class BankAccount implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(min = 16, max = 16)
    @Column(name = "card_number", length = 16, nullable = false, unique = true)
    private String cardNumber;

    @NotNull
    @Column(name = "open_date", nullable = false)
    private LocalDate openDate;

    @Column(name = "closing_date")
    private LocalDate closingDate;

    @NotNull
    @Column(name = "balance", precision = 21, scale = 2, nullable = false)
    private BigDecimal balance;

    @ManyToOne
    private User user;

    @OneToMany(mappedBy = "fromAccount")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "fromAccount", "toAccount" }, allowSetters = true)
    private Set<BankTransfer> transfersOuts = new HashSet<>();

    @OneToMany(mappedBy = "toAccount")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "fromAccount", "toAccount" }, allowSetters = true)
    private Set<BankTransfer> transfersIns = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public BankAccount id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCardNumber() {
        return this.cardNumber;
    }

    public BankAccount cardNumber(String cardNumber) {
        this.setCardNumber(cardNumber);
        return this;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public LocalDate getOpenDate() {
        return this.openDate;
    }

    public BankAccount openDate(LocalDate openDate) {
        this.setOpenDate(openDate);
        return this;
    }

    public void setOpenDate(LocalDate openDate) {
        this.openDate = openDate;
    }

    public LocalDate getClosingDate() {
        return this.closingDate;
    }

    public BankAccount closingDate(LocalDate closingDate) {
        this.setClosingDate(closingDate);
        return this;
    }

    public void setClosingDate(LocalDate closingDate) {
        this.closingDate = closingDate;
    }

    public BigDecimal getBalance() {
        return this.balance;
    }

    public BankAccount balance(BigDecimal balance) {
        this.setBalance(balance);
        return this;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public BankAccount user(User user) {
        this.setUser(user);
        return this;
    }

    public Set<BankTransfer> getTransfersOuts() {
        return this.transfersOuts;
    }

    public void setTransfersOuts(Set<BankTransfer> bankTransfers) {
        if (this.transfersOuts != null) {
            this.transfersOuts.forEach(i -> i.setFromAccount(null));
        }
        if (bankTransfers != null) {
            bankTransfers.forEach(i -> i.setFromAccount(this));
        }
        this.transfersOuts = bankTransfers;
    }

    public BankAccount transfersOuts(Set<BankTransfer> bankTransfers) {
        this.setTransfersOuts(bankTransfers);
        return this;
    }

    public BankAccount addTransfersOut(BankTransfer bankTransfer) {
        this.transfersOuts.add(bankTransfer);
        bankTransfer.setFromAccount(this);
        return this;
    }

    public BankAccount removeTransfersOut(BankTransfer bankTransfer) {
        this.transfersOuts.remove(bankTransfer);
        bankTransfer.setFromAccount(null);
        return this;
    }

    public Set<BankTransfer> getTransfersIns() {
        return this.transfersIns;
    }

    public void setTransfersIns(Set<BankTransfer> bankTransfers) {
        if (this.transfersIns != null) {
            this.transfersIns.forEach(i -> i.setToAccount(null));
        }
        if (bankTransfers != null) {
            bankTransfers.forEach(i -> i.setToAccount(this));
        }
        this.transfersIns = bankTransfers;
    }

    public BankAccount transfersIns(Set<BankTransfer> bankTransfers) {
        this.setTransfersIns(bankTransfers);
        return this;
    }

    public BankAccount addTransfersIn(BankTransfer bankTransfer) {
        this.transfersIns.add(bankTransfer);
        bankTransfer.setToAccount(this);
        return this;
    }

    public BankAccount removeTransfersIn(BankTransfer bankTransfer) {
        this.transfersIns.remove(bankTransfer);
        bankTransfer.setToAccount(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BankAccount)) {
            return false;
        }
        return id != null && id.equals(((BankAccount) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BankAccount{" +
            "id=" + getId() +
            ", cardNumber='" + getCardNumber() + "'" +
            ", openDate='" + getOpenDate() + "'" +
            ", closingDate='" + getClosingDate() + "'" +
            ", balance=" + getBalance() +
            "}";
    }
}
