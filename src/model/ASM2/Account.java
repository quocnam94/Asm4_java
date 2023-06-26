package model.ASM2;

import dao.TransactionDAO;
import model.Transaction;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Account implements Serializable {
    public static final long serialVersionUID = 0L;
    private final String customerId;
    private final String accountNumber;
    private double balance;

    public Account(String customerId, String accountNumber, double balance) {
        this.customerId = customerId;
        this.accountNumber = accountNumber;
        this.balance = balance;
    }

    public boolean isAccountPremium() {
        return getBalance() >= 10000000;
    }
    public String getAccountNumber() {
        return accountNumber;
    }
    public double getBalance() {
        return this.balance;
    }
    public String getCustomerId() {
        return customerId;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }
// xac dinh so tai khoan co hop le khong
    public static boolean validAccount(String accountNumber){
        Pattern pattern = Pattern.compile("^\\d{6}$");
        return pattern.matcher(accountNumber).find();
    }
    public String toString(String accountType) {
        return String.format("%11s |  %-15s | %,29.0f đ", accountNumber, accountType, getBalance());
    }
//    lay cac giao dich cua 1 tai khoan
    public List<Transaction> getTransactions() {
        List<Transaction> transactions = TransactionDAO.list();
        return transactions.stream()
                .filter(transaction -> transaction.getAccountNumber().equals(this.accountNumber))
                .collect(Collectors.toList());
    }
//    them giao dich
    public void createTransaction(Transaction newTransaction) throws IOException {
        List<Transaction> transactions = TransactionDAO.list();
        transactions.add(newTransaction);
        TransactionDAO.save(transactions);
    }
//    hiew thi danh sach giao dich
    public void displayTransactionList() {
        for (Transaction transaction : getTransactions()) {
            System.out.println(transaction.toString());
        }
    }
}
