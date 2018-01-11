package com.luxoft.bank.bankaccount;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

@SpringBootApplication
@EntityScan(
        basePackageClasses = {BankaccountApplication.class, Jsr310JpaConverters.class}
)
public class BankaccountApplication {

    public static void main(String[] args) {
        SpringApplication.run(BankaccountApplication.class, args);
    }
}
