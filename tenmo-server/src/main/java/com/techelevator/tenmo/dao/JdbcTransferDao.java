package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.exception.DaoException;
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

    public JdbcTransferDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // may not need all transfer methods check later
    @Override
    public List<Transfer> getTransfers(int accountId) {
        List<Transfer> transfers = new ArrayList<>();
        String sql = "SELECT * FROM transfers " +
                "WHERE account_from = ? OR account_to = ?;";
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, accountId, accountId);
            if (results.next()) {
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
    public Transfer getTransferById(int transferId) {
        Transfer transfer = null;
        String sql = "SELECT * from transfer " +
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



    @Override
    public List<Transfer> getTransfersReceivedById(int accountTo) {
        List<Transfer> transfers = new ArrayList<>();
        String sql = "SELECT * FROM transfer " +
                "WHERE account_to = ?;";
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, accountTo);
            if (results.next()) {
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
    public List<Transfer> getTransfersSentById(int accountFrom) {
        List<Transfer> transfers = new ArrayList<>();
        String sql = "SELECT * FROM transfer " +
                "WHERE account_from = ?;";
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, accountFrom);
            if (results.next()) {
                Transfer transfer = mapToTransfer(results);
                transfers.add(transfer);
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        } return transfers;
    }
    private Transfer mapToTransfer(SqlRowSet sqlRowSet) {
        Transfer transfer = new Transfer();
        transfer.setTransferStatus(sqlRowSet.getInt("transfer_status_id"));
        transfer.setAccountFrom(sqlRowSet.getInt("account_from"));
        transfer.setAccountTo(sqlRowSet.getInt("account_to"));
        transfer.setAmount(sqlRowSet.getBigDecimal("amount"));
        transfer.setId(sqlRowSet.getInt("transfer_id"));
        return transfer;
    }
}
