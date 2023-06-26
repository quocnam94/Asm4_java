package model;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

public class Transaction implements Serializable {
    private final String accountNumber;
    private final double amount;
    private final String time;
    private final TransactionType types;
    @Serial
    private static final long serialVersionUID = 0;

    public enum TransactionType {
        DEPOSIT,
        WITHDRAW,
        TRANSFERS,
    }

    public Transaction(TransactionType type, String accountNumber, Date time, double amount, boolean status) {
        this.time = String.valueOf(time);
        this.accountNumber = accountNumber;
        this.amount = amount;
        this.types = type;
    }
    public String getAccountNumber() {
        return accountNumber;
    }

    public String toString() {
        return String.format("[GD]  %s |  %s | %,10.0fđ |  %s  ", accountNumber, types, amount, time);
    }
}
