package model;

import dao.AccountDAO;
import model.ASM2.Account;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

public class SavingsAccount extends Account implements IReport, IWithdraw, ITransfer {
//    public static final long serialVersionUID = 0L;

    public SavingsAccount(String customerId, String accountNumber, double balance) {
        super(customerId, accountNumber, balance);
    }
//    kiem tra dieu kien rut tien
    @Override
    public void withdraw(double amount){
        if (isAccepted(amount)) {
            double newBalance = getBalance() - amount;
            setBalance(newBalance);
        }
    }
    @Override
    public boolean isAccepted(double amount) {
        if (amount >= 50000 && amount % 10000 == 0 && (getBalance() - amount >= 50000)) {
            if (!isPremium()) {
                double SAVING_ACCOUNT_MAX_WITHDRAW = 5000000.0;
                return amount <= SAVING_ACCOUNT_MAX_WITHDRAW;
            } else {
                return true;
            }
        }
        return false;
    }
//    bien lai chuyen tien
    @Override
    public void logTransfers (double amount){
        long millis = System.currentTimeMillis();
        java.util.Date date = new java.util.Date(millis);
        System.out.println("+------+-----------------------+------+");
        System.out.println("      BIEN LAI GIAO DICH SAVINGS       ");
        System.out.printf("NGAY GD:  %28s%n", date);
        System.out.printf("ATM ID: %30s%n", "DIGITAL-BANK-ATM 2023");
        System.out.printf("SO TK: %31s%n", getAccountNumber());
        System.out.printf("SO TIEN CHUYEN: %22s%n", String.format("%.1f", amount) + "d");
        System.out.printf("SO DU TK: %28s%n", String.format("%.1f", getBalance()) + "d");
        System.out.printf("Phi + VAT:             %15s\n", "0d");
        System.out.println("+------+-----------------------+------+");
    }
    //    bien lai rut tien
    @Override
    public void logWithdraw (double amount){
        long millis = System.currentTimeMillis();
        java.util.Date date = new java.util.Date(millis);
        System.out.println("+------+-----------------------+------+");
        System.out.println("      BIEN LAI GIAO DICH SAVINGS       ");
        System.out.printf("NGAY GD:  %28s%n", date);
        System.out.printf("ATM ID: %30s%n", "DIGITAL-BANK-ATM 2023");
        System.out.printf("SO TK: %31s%n", getAccountNumber());
        System.out.printf("SO TIEN RUT: %25s%n", String.format("%.1f", amount) + "d");
        System.out.printf("SO DU TK: %28s%n", String.format("%.1f", getBalance()) + "d");
        System.out.printf("Phi + VAT:             %15s\n", "0d");
        System.out.println("+------+-----------------------+------+");
    }

    public List<Account> getAccounts() {
        List<Account> accountList = AccountDAO.list();
        return accountList.stream()
                .filter(account -> account.getCustomerId().equals(this.getCustomerId()))
                .collect(Collectors.toList());
    }
    public boolean isPremium() {
        for (Account account : getAccounts()) {
            if (account.isAccountPremium()) {
                return true;
            }
        }
        return false;
    }
//    chuyen tien
    @Override
    public void transfer(Account receiveAccount, double amount) {
        if (isAccepted(amount)) {
            super.setBalance(getBalance() - amount);
            receiveAccount.setBalance(receiveAccount.getBalance() + amount);
        }
    }
//    tao lich su rut tien
    public void withdrawSavingsTransaction(boolean isSuccess, double amount) throws IOException {
        if (isSuccess) {
            long millis = System.currentTimeMillis();
            java.util.Date date = new java.util.Date(millis);
            System.out.println("Rut tien thanh cong, bien lai giao dich: ");
            Transaction newWithdrawSavingsTransaction = new Transaction(Transaction.TransactionType.WITHDRAW, getAccountNumber(), date, 0 - amount, true);
            createTransaction(newWithdrawSavingsTransaction);
        } else {
            System.out.println("Rut tien that bai");
        }
    }
    //    tao lich su chuyen tien
    public void transferSavingsTransaction(boolean isSuccess, Account receiveAccount, double amount) throws IOException {
        if (isSuccess) {
            long millis = System.currentTimeMillis();
            java.util.Date date = new java.util.Date(millis);
            System.out.println("Chuyen tien thanh cong, bien lai giao dich: ");
            Transaction sendTransferTransaction = new Transaction(Transaction.TransactionType.TRANSFERS, getAccountNumber(), date, 0 - amount, true);
            this.createTransaction(sendTransferTransaction);
            Transaction receiveTransferTransaction = new Transaction(Transaction.TransactionType.TRANSFERS, receiveAccount.getAccountNumber(), date, amount, true);
            receiveAccount.createTransaction(receiveTransferTransaction);
        } else {
            System.out.println("Chuyen tien that bai");
        }
    }
}
