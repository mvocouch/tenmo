package com.techelevator.dao;

import com.techelevator.tenmo.dao.JdbcTransferDao;
import com.techelevator.tenmo.model.Transfer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import java.math.BigDecimal;
import java.util.List;

public class JdbcTransferDaoTests extends BaseDaoTests{
    protected static final Transfer TRANSFER_1 = new Transfer(1001, 1, 1, 2,  BigDecimal.valueOf(1500));
    //protected static final Transfer TRANSFER_2 = new Transfer(1002, 1, 1, 2,  BigDecimal.valueOf(1000));
    protected static final Transfer TRANSFER_3 = new Transfer(1003, 1, 1, 2,  BigDecimal.valueOf(500));
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
        List<Transfer> transfers = sut.findAll();
        Assert.assertNotNull(transfers);
        Assert.assertEquals(2, transfers.size());
        Assert.assertEquals(TRANSFER_1, transfers.get(0));
        Assert.assertEquals(TRANSFER_3, transfers.get(1));

    }
}

