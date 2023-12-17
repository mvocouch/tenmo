package com.techelevator.dao;

import com.techelevator.tenmo.dao.JdbcTransferDao;
import com.techelevator.tenmo.model.Transfer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import java.math.BigDecimal;

public class JdbcTransferDaoTests extends BaseDaoTests{
    //protected static final Transfer TRANSFER_1 = new Transfer(1001, 1, 1, 1, 2,  1500);
    private JdbcTransferDao sut;

    @Before
    public void setup() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        sut = new JdbcTransferDao(jdbcTemplate);
    }
    @Test
    public void getTransferById_given_invalid_id_returns_null(){
       Transfer transfer = sut.getTransferById(-1);
        Assert.assertNull(transfer);
    }
    @Test
    public void getTransfers_returns_all_transfers() {

    }
}

