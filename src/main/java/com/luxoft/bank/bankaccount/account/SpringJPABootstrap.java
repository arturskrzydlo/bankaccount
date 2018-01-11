package com.luxoft.bank.bankaccount.account;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.time.YearMonth;

@Component class SpringJPABootstrap implements ApplicationListener<ContextRefreshedEvent> {

    private AccountRepository accountRepository;
    private AccountOperationRepository accountOperationRepository;
    private AccountFacade accountFacade;

    @Autowired
    public void setAccountRepository(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Autowired
    public void setAccountOperationRepository(AccountOperationRepository accountOperationRepository) {
        this.accountOperationRepository = accountOperationRepository;
    }

    @Autowired
    public void setAccountFacade(AccountFacade accountFacade) {
        this.accountFacade = accountFacade;
    }

    @Override public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        loadAccounts();
        try {
            doSomeAccountOperations();
        } catch (NotSufficientFundsException e) {
            e.printStackTrace();
        } catch (NoSuchAccountException e) {
            e.printStackTrace();
        }
    }

    private void doSomeAccountOperations() throws NotSufficientFundsException, NoSuchAccountException {
        accountFacade.deposit(1, 400);
        accountFacade.withdraw(1, 200);

        accountFacade.transfer(2, 1, 200.0);

        AccountStatement accountStatement = accountFacade.getAccountStatement(1, YearMonth.now());

    }

    private void loadAccounts() {

        Account sourAccount = new Account();
        sourAccount.setBalance(200.0);
        sourAccount.setAccountType(AccountType.DEBIT);

        accountRepository.save(sourAccount);

        Account destinationAccount = new Account();
        destinationAccount.setBalance(200.0);
        destinationAccount.setAccountType(AccountType.CREDIT);

        accountRepository.save(destinationAccount);
    }
}
