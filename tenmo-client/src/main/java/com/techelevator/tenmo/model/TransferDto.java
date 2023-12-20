package com.techelevator.tenmo.model;

import java.math.BigDecimal;

public class TransferDto {

        private long userFrom;
        private long userTo;
        private BigDecimal amount;
        private int transferType;

        public TransferDto(long fromUserId, long toUserId, BigDecimal amount, int transferType) {
            this.userFrom = fromUserId;
            this.userTo = toUserId;
            this.amount = amount;
            this.transferType = transferType;
        }

        public long getUserFrom() {
            return userFrom;
        }

        public void setUserFrom(long userFrom) {
            this.userFrom = userFrom;
        }

        public long getUserTo() {
            return userTo;
        }

        public void setUserTo(long userTo) {
            this.userTo = userTo;
        }

        public BigDecimal getAmount() {
            return amount;
        }

        public void setAmount(BigDecimal amount) {
            this.amount = amount;
        }

        public int getTransferType() {
            return transferType;
        }


}
