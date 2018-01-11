package com.luxoft.bank.bankaccount.account

import spock.lang.Specification

import java.lang.Void as Should

class TransferSpec extends Specification {

    def sourceAccount = createSampleDebitAccount()
    def destinationAccount = createSampleDebitAccount()

    def accountRepository = Mock(AccountRepository)
    def accountOperationRepository = Mock(AccountOperationRepository)
    def accountService = new AccountServiceImpl(accountRepository, accountOperationRepository)

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

    def "when trasnferring, source amount balanced must be decreased and destination account must be increased exacly by transfer amount"() {

        given: "initial accout balanace values and expected results after transfer"
            def moneyToTransfer = 200
            def sourceAccountInitialBalance = sourceAccount.getBalance()
            def destinationAccountInitialBalance = destinationAccount.getBalance()
            def sourceBalanceExpected = sourceAccountInitialBalance - moneyToTransfer
            def destinationBalanceExpected = destinationAccountInitialBalance + moneyToTransfer

        and:

            def modifiedSourceAccount = createSampleDebitAccount()
            modifiedSourceAccount.setBalance(sourceBalanceExpected)

            def modifiedDestinationAccount = createSampleDebitAccount()
            modifiedDestinationAccount.setBalance(destinationBalanceExpected)

            accountRepository.findOne(sourceAccount.getId()) >> sourceAccount
            accountRepository.findOne(destinationAccount.getId()) >> destinationAccount

        when:
            "transfer #moneyToTransfer between two accounts"
            def result = accountService.transfer(sourceAccount.getId(), destinationAccount.getId(), moneyToTransfer)

        then: "both accounts balances has been correctly passed to the repository"
            1 * accountRepository.save(sourceAccount) >> modifiedSourceAccount
            1 * accountRepository.save(destinationAccount) >> modifiedDestinationAccount
            2 * accountOperationRepository.save(_)
        and: "returned result is remaining funds in source account"
            result == sourceBalanceExpected
        and: "remaining funds are not less then 0"
            result >= 0

    }


    def "when source account doesn't exists throw NoSuchAccountException"() {

        given: "existing destination account and not existing source account"
            def moneyToTransfer = 200.0
            def sourceAccount = sourceAccount
            accountRepository.findOne(sourceAccount.getId()) >> null
            accountRepository.findOne(destinationAccount.getId()) >> destinationAccount

        when: "transferring money from not existing account to existing account"
            accountService.transfer(sourceAccount.getId(), destinationAccount.getId(), moneyToTransfer)

        then:
            NoSuchAccountException exc = thrown()
            exc.message == "Account with id " + sourceAccount.getId() + " doesnt exists"
    }

    def "when destination account doesn't exists throw NoSuchAccountException"() {

        given: "existing source account and not existing destination account"
            def moneyToTransfer = 200.0
            def destinationAccount = destinationAccount
            accountRepository.findOne(destinationAccount.getId()) >> null
            accountRepository.findOne(sourceAccount.getId()) >> sourceAccount

        when: "transferring money from not existing account to existing account"
            accountService.transfer(sourceAccount.getId(), destinationAccount.getId(), moneyToTransfer)

        then:
            NoSuchAccountException exc = thrown()
            exc.message == "Account with id " + destinationAccount.getId() + " doesnt exists"
    }


    def createSampleDebitAccount() {
        def account = new Account()
        account.setAccountType(AccountType.DEBIT)
        return account
    }
}
