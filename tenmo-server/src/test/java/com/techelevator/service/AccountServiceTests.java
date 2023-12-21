package com.techelevator.service;

import com.techelevator.dao.MemoryAccountDao;
import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.service.AccountServiceImpl;
import com.techelevator.tenmo.services.AccountService;
import org.junit.Before;

public class AccountServiceTests {
    private AccountServiceImpl accountService;
    private AccountDao accountDao;

    public AccountServiceTests() {
        this.accountDao = new MemoryAccountDao();
        this.accountService = new AccountServiceImpl(accountDao, null);
    }

    @Before
    public void setup(){

    }
}
