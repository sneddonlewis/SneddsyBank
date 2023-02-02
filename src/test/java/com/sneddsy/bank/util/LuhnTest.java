package com.sneddsy.bank.util;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

public class LuhnTest {

    @Test
    void validateCheckSum() {
        String invalidCheck = "2000007269641764";
        String validCheck = "2000007269641768";
        assertThat(Luhn.validateCheckSum(invalidCheck)).isFalse();
        assertThat(Luhn.validateCheckSum(validCheck)).isTrue();
    }
}
