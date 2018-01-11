package com.luxoft.bank.bankaccount.account.acceptance

import com.luxoft.bank.bankaccount.account.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification

import java.lang.Void as Should
import java.time.YearMonth

@SpringBootTest
class GettingAccountStatementSpec extends Specification {

    @Autowired
    private AccountRepository accountRepository

    @Autowired
    private AccountFacade accountFacade

    @Autowired
    private AccountOperationRepository accountOperationRepository

    def sourceAccount = new Account()
    def destinationAccount = new Account()

    def setup() {
        loadAccounts()
    }

    Should "sucesfully get filled account statement after two previous financial operations, which should be included in this account statement "() {
        given: "on checked account deposit operation has been invoked"
            accountFacade.deposit(sourceAccount.getId(), 400)

        and: "on checked account transfer operation has been invoked"
            accountFacade.transfer(sourceAccount.getId(), destinationAccount.getId(), 100)

        when: "someone get current account statement for checked account"
            def accountStatement = accountFacade.getAccountStatement(sourceAccount.getId(), YearMonth.now())

        then: "Account statement is produced for current date"
            accountStatement.statementYearMonth == (YearMonth.now())
        and: "Account statement contains exatcly two operations"
            accountStatement.operations.size() == 2
        and: "Account statement contains one deposit operation"
            Optional<AccountOperation> depositOp = accountStatement.operations
                    .stream()
                    .filter({ it.operationType == AccountOperationType.DEPOSIT })
                    .findAny()

            depositOp.isPresent()

        and: "Account statement contains one transfer operation"
            Optional<AccountOperation> transferOp = accountStatement.operations
                    .stream()
                    .filter({ it.operationType == AccountOperationType.DEPOSIT })
                    .findAny()

            transferOp.isPresent()
        and: "Account statement was done for correct account"
            accountStatement.getAccountId() == sourceAccount.getId()
    }


    def loadAccounts() {

        sourceAccount = new Account()
        sourceAccount.setBalance(200.0)
        sourceAccount.setAccountType(AccountType.DEBIT)

        accountRepository.save(sourceAccount)

        destinationAccount = new Account()
        destinationAccount.setBalance(200.0)
        destinationAccount.setAccountType(AccountType.CREDIT)

        accountRepository.save(destinationAccount)
    }

}
