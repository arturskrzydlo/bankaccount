package com.luxoft.bank.bankaccount

import com.luxoft.bank.bankaccount.account.*
import spock.lang.Specification

import java.lang.Void as Should

class WithdrawalDepositSpec extends Specification {

    def debitAccount = createSampleDebitAccount()
    def creditAccount = createSampleCreditAccount()

    def accountId = 1
    def accountRepository = Mock(AccountRepository)
    def accountOperationRepository = Mock(AccountOperationRepository)
    def accountService = new AccountServiceImpl(accountRepository, accountOperationRepository)

    Should "after withdrawal balance should be substracted with amount of withdrawal"() {

        given: "accont with #initialBalance balance"
            debitAccount.setBalance(initialBalance);

        and: "prepared result from db"
            def modifiedAccount = createSampleDebitAccount()
            modifiedAccount.setBalance(expectedResult)

        when: "withdraw #withdrawal then amount should be #expectedResult"
            def result = accountService.withdraw(accountId, withdrawal)

        then:
            "returned balance is #expectedResult"
            result == expectedResult
        and:
            1 * accountRepository.save(debitAccount) >> modifiedAccount
            1 * accountRepository.findOne(_) >> debitAccount
            1 * accountOperationRepository.save(_)
        where:
            initialBalance | withdrawal || expectedResult
            200.0          | 100.0      || 100.0
            200.0          | 150.0      || 50.0
            0              | 0           | 0

    }

    Should "throw NoSufficientFundsException when withdrawal exceed  debit account balance "() {

        given: "withdrawal which exceedes initial balance (which is zero)"
            def withDrawalWhichExceedesBalance = 100
            accountRepository.findOne(accountId) >> debitAccount

        when:
            accountService.withdraw(accountId, withDrawalWhichExceedesBalance)

        then:
            NotSufficientFundsException exception = thrown()
            exception.message == "You exceedes your account balance for " + withDrawalWhichExceedesBalance
    }

    Should "not throw exception when withdrawal exceedes credit card"() {

        given: "withdrawal which exceedes initial balance (which is zero)"
            def withDrawalWhichExceedesBalance = 100.0
            accountRepository.findOne(accountId) >> creditAccount
            accountRepository.save(creditAccount) >> creditAccount

        when:
            accountService.withdraw(accountId, withDrawalWhichExceedesBalance)

        then:
            notThrown(NotSufficientFundsException)

    }

    Should "deposit increase balance exactly by deposit amount"() {

        given: "account with #initialBalance balance"
            debitAccount.setBalance(initialBalance)
            accountRepository.findOne(accountId) >> debitAccount

        and: "prepared result from db"
            def modifiedAccount = createSampleDebitAccount()
            modifiedAccount.setBalance(expectedResult)

        when: "deposit #deposit"
            def result = accountService.deposit(accountId, deposit)

        then:
            "returned balance is #expectedResult"
            result == expectedResult
        and:
            1 * accountRepository.save(_) >> modifiedAccount
            1 * accountRepository.findOne(_) >> debitAccount
            1 * accountOperationRepository.save(_)
        where:
            initialBalance | deposit || expectedResult
            200.0          | 100.0   || 300.0
            200.0          | 0       || 200.0
            0              | 0        | 0
    }


    def createSampleDebitAccount() {
        def account = new Account()
        account.setAccountType(AccountType.DEBIT)
        return account
    }

    def createSampleCreditAccount() {
        def account = new Account()
        account.setAccountType(AccountType.CREDIT)
        return account
    }
}
