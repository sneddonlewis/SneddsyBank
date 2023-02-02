package com.sneddsy.bank.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.sneddsy.bank.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class BankTransferTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(BankTransfer.class);
        BankTransfer bankTransfer1 = new BankTransfer();
        bankTransfer1.setId(1L);
        BankTransfer bankTransfer2 = new BankTransfer();
        bankTransfer2.setId(bankTransfer1.getId());
        assertThat(bankTransfer1).isEqualTo(bankTransfer2);
        bankTransfer2.setId(2L);
        assertThat(bankTransfer1).isNotEqualTo(bankTransfer2);
        bankTransfer1.setId(null);
        assertThat(bankTransfer1).isNotEqualTo(bankTransfer2);
    }
}
