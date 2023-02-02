package com.sneddsy.bank.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A BankTransfer.
 */
@Entity
@Table(name = "bank_transfer")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class BankTransfer implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "amount", precision = 21, scale = 2)
    private BigDecimal amount;

    @Column(name = "execution_time")
    private ZonedDateTime executionTime;

    @ManyToOne
    @JsonIgnoreProperties(value = { "user", "transfersOuts", "transfersIns" }, allowSetters = true)
    private BankAccount fromAccount;

    @ManyToOne
    @JsonIgnoreProperties(value = { "user", "transfersOuts", "transfersIns" }, allowSetters = true)
    private BankAccount toAccount;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public BankTransfer id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getAmount() {
        return this.amount;
    }

    public BankTransfer amount(BigDecimal amount) {
        this.setAmount(amount);
        return this;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public ZonedDateTime getExecutionTime() {
        return this.executionTime;
    }

    public BankTransfer executionTime(ZonedDateTime executionTime) {
        this.setExecutionTime(executionTime);
        return this;
    }

    public void setExecutionTime(ZonedDateTime executionTime) {
        this.executionTime = executionTime;
    }

    public BankAccount getFromAccount() {
        return this.fromAccount;
    }

    public void setFromAccount(BankAccount bankAccount) {
        this.fromAccount = bankAccount;
    }

    public BankTransfer fromAccount(BankAccount bankAccount) {
        this.setFromAccount(bankAccount);
        return this;
    }

    public BankAccount getToAccount() {
        return this.toAccount;
    }

    public void setToAccount(BankAccount bankAccount) {
        this.toAccount = bankAccount;
    }

    public BankTransfer toAccount(BankAccount bankAccount) {
        this.setToAccount(bankAccount);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BankTransfer)) {
            return false;
        }
        return id != null && id.equals(((BankTransfer) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BankTransfer{" +
            "id=" + getId() +
            ", amount=" + getAmount() +
            ", executionTime='" + getExecutionTime() + "'" +
            "}";
    }
}
