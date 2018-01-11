package com.luxoft.bank.bankaccount

import com.luxoft.bank.bankaccount.account.Account
import com.luxoft.bank.bankaccount.account.AccountRepository
import com.luxoft.bank.bankaccount.account.AccountServiceImpl
import com.luxoft.bank.bankaccount.account.NotSufficientFundsException
import spock.lang.Specification

import java.lang.Void as Should

class TransferSpec extends Specification {

    def sourceAccount = createSampleDebitAccount()
    def destinationAccount = createSampleDebitAccount()

    def accountRepository = Mock(AccountRepository)
    def accountService = new AccountServiceImpl(accountRepository)

    def setup() {
        sourceAccount.setId(1)
        destinationAccount.setId(2)
        sourceAccount.setBalance(500)
        destinationAccount.setBalance(500)

    }

    Should "when no sufficient funds on source account, then throw NoSufficientFundsException"() {

        given: "source account with balance smaller than transferred amount of money"
            def sourceAccountInitialBalance = 100
            sourceAccount.setBalance(sourceAccountInitialBalance)
            sourceAccount.setId(1)
            destinationAccount.setId(2)
            def moneyToTransfer = 200
            def moneyWhichExceedesBalance = -(sourceAccountInitialBalance - moneyToTransfer)

        and:
            accountRepository.findOne(1) >> sourceAccount
            accountRepository.findOne(2) >> destinationAccount

        when: "sending smaller amount money than source account balance"
            accountService.transfer(sourceAccount.getId(), destinationAccount.getId(), moneyToTransfer)

        then: "NoSufficientFundsException is thrown"
            NotSufficientFundsException exception = thrown()
            exception.message == "You exceedes your account balance for " + moneyWhichExceedesBalance

    }


    def createSampleDebitAccount() {
        def account = new Account()
        account.setAccountType(AccountType.DEBIT)
        return account
    }
}
