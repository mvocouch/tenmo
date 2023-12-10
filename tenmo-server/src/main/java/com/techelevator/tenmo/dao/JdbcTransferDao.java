package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.exception.DaoException;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.relational.core.sql.Where;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import java.util.ArrayList;
import java.util.List;

public class JdbcTransferDao implements TransferDao{

    private final JdbcTemplate jdbcTemplate;
    private UserDao userDao;
    private AccountDao accountDao;
    private static final String SQL_SELECT_TRANSFER = "SELECT t.transfer_id, tt.transfer_type_desc, ts.transfer_status_desc, t.amount, " +
            "aFrom.account_id AS fromAccount, aFrom.user_id AS fromUser, aFrom.balance AS fromBalance " +
            "aTo.account_id As toAccount, aTo.user_id AS toUser, aTo.balance AS toBalance " +
            "FROM transfer t " +
            "JOIN transfer_type tt USING (transfer_type_id) " +
            "JOIN transfer_status ts USING (transfer_status_id) " +
            "JOIN account aFrom ON t.account_from = aFrom.account_id " +
            "JOIN account aTo ON t.account_to = aTo.account_id ";


    public JdbcTransferDao(JdbcTemplate jdbcTemplate, UserDao userDao, AccountDao accountDao) {
        this.jdbcTemplate = jdbcTemplate;
        this.userDao = userDao;
        this.accountDao = accountDao;
    }


    @Override
    public List<Transfer> findAll() {
        List<Transfer> transfers = new ArrayList<>();
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(SQL_SELECT_TRANSFER);
            while (results.next()) {
                Transfer transfer = mapToTransfer(results);
                transfers.add(transfer);
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        } return transfers;
    }

    @Override
    public List<Transfer> getTransfersForUser(int userId) {
        List<Transfer> transfers = new ArrayList<>();
        String sql = SQL_SELECT_TRANSFER +
                "WHERE (account_from IN (SELECT account_id FROM account WHERE user_id = ?) " +
                "OR  (account_to IN (SELECT account_id FROM account WHERE user_id = ?);";

        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId, userId);
            while (results.next()) {
                Transfer transfer = mapToTransfer(results);
                transfers.add(transfer);
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        } return transfers;
    }

    @Override
   public Transfer addTransfer(Transfer newtransfer) {
//
//    String sql = "INSERT INTO transfer (transfer_id, transfer_type_id, transfer_status_id, account_from, account_to, amount) " +
//            "VALUES (?, ?, ?, ?, ?, ?);";
//       Long newTransferId = getNextTransferId();
//       Long transferTypeId = getTransferTypeId(newtransfer.getTransferType());
//       Long transferStatusId = getTransferStatus(newtransfer.getTransferStatus());
//       //will create account here using userIds from newTransfer
//       // need account dao Account fromAccount = accountdao. get account from user id, newTransfer will have user from and user to
//        // need account dao Account toAccount = accountdao. get account from user id, newTransfer will have user from and user to
//        try {
//            jdbcTemplate.update(sql, newTransferId, transferTypeId, transferStatusId, newtransfer.getAccountFrom(), newtransfer.getAccountTo(), newtransfer.getAmount());
//        } catch (CannotGetJdbcConnectionException e) {
//            throw new DaoException("Unable to connect to server or database", e);
//        } catch (DataIntegrityViolationException e) {
//            throw new DaoException("Data integrity violation", e);
//        }
//        return getTransferById(newTransferId);
        return newtransfer; //here for the sake of happy intellij
    }

    @Override
    public Transfer getTransferById(long transferId) {
        Transfer transfer = null;
        String sql = SQL_SELECT_TRANSFER +
                "WHERE transfer_id = ?;";
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, transferId);
            if (results.next()) {
                transfer = mapToTransfer(results);
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        } return transfer;
    }
    private long getNextTransferId() {
        String sql = "SELECT MAX(transfer_id) from transfer;";
        Long maxId = jdbcTemplate.queryForObject(sql, Long.class);
        if (maxId != null) {
            return maxId + 1;
        } else return 1;
    }
    private Long getTransferTypeId(String transferType) {
        String sql = "SELECT transfer_id FROM transfer_type WHERE transfer_type_desc = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, transferType);
        if (results.next()) {
            //gets column based in number provided, this gives first column
            return results.getLong(1);
        } else {
            throw new RuntimeException("Cannot find transfer type : " + transferType);
        }
    }
    private Long getTransferStatus(String transferStatus) {
        String sql = "SELECT transfer_id FROM transfer_status WHERE transfer_status_desc = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, transferStatus);
        if (results.next()) {
            return results.getLong(1);
        } else {
            throw new RuntimeException("Cannot find transfer type : " + transferStatus);
        }
    }

    private Transfer mapToTransfer(SqlRowSet sqlRowSet) {
        Transfer transfer = new Transfer();
        transfer.setTransferStatus(sqlRowSet.getInt("transfer_status_desc"));
        transfer.setAccountFrom(userDao.getUserById(sqlRowSet.getInt("fromUser")).getId());
        // should transfer hold the whole user class or just the id
        //ie User userFrom and User userTo

        transfer.setAccountTo(userDao.getUserById(sqlRowSet.getInt("toUser")).getId());
        transfer.setAmount(sqlRowSet.getBigDecimal("amount"));
        transfer.setId(sqlRowSet.getLong("transfer_id"));
        return transfer;

    }

        // old code, will delete if not using

//    @Override
//    public List<Transfer> getTransfersReceivedById(int accountTo) {
//        List<Transfer> transfers = new ArrayList<>();
//        String sql = "SELECT * FROM transfer " +
//                "WHERE account_to = ?;";
//        try {
//            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, accountTo);
//            if (results.next()) {
//                Transfer transfer = mapToTransfer(results);
//                transfers.add(transfer);
//            }
//        } catch (CannotGetJdbcConnectionException e) {
//            throw new DaoException("Unable to connect to server or database", e);
//        } catch (DataIntegrityViolationException e) {
//            throw new DaoException("Data integrity violation", e);
//        } return transfers;
//    }

//    @Override
//    public List<Transfer> getTransfersSentById(int accountFrom) {
//        List<Transfer> transfers = new ArrayList<>();
//        String sql = "SELECT * FROM transfer " +
//                "WHERE account_from = ?;";
//        try {
//            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, accountFrom);
//            if (results.next()) {
//                Transfer transfer = mapToTransfer(results);
//                transfers.add(transfer);
//            }
//        } catch (CannotGetJdbcConnectionException e) {
//            throw new DaoException("Unable to connect to server or database", e);
//        } catch (DataIntegrityViolationException e) {
//            throw new DaoException("Data integrity violation", e);
//        } return transfers;
//    }


    // may not need all transfer methods check later
    //   @Override
//    public List<Transfer> getTransfers(int accountId) {
//        List<Transfer> transfers = new ArrayList<>();
//        String sql = "SELECT * FROM transfers " +
//                "WHERE account_from = ? OR account_to = ?;";
//        try {
//            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, accountId, accountId);
//            if (results.next()) {
//                Transfer transfer = mapToTransfer(results);
//                transfers.add(transfer);
//            }
//        } catch (CannotGetJdbcConnectionException e) {
//            throw new DaoException("Unable to connect to server or database", e);
//        } catch (DataIntegrityViolationException e) {
//            throw new DaoException("Data integrity violation", e);
//        } return transfers;
//    }
}
