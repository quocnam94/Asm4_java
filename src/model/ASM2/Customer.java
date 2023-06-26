package model.ASM2;

import dao.AccountDAO;
import model.*;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import static model.ASM2.Account.validAccount;

public class Customer extends User implements Serializable {
    private final String name;
    private String customerId;
    public static final long serialVersionUID = 0L;

    public Customer(String customerId, String name) {
        this.name = name;
        if (validCustomerId(customerId)) {
            this.customerId = customerId;
        } else {
            System.out.println("Ma khach hang khong hop le");
        }
    }

    public Customer(List<String> values) {
        this(values.get(0), values.get(1));
    }

    public String getName() {
        return name;
    }

    public String getCustomerId() {
        return customerId;
    }
// lay các tai khoan trong 1 ma khach hang
    public List<Account> getAccounts() {
        List<Account> accountList = AccountDAO.list();
        return accountList.stream()
                .filter(account -> account.getCustomerId().equals(this.customerId))
                .collect(Collectors.toList());
    }
    // them tai khoan moi
    public void addAccount(Account newAccount) {
        try {
            long millis = System.currentTimeMillis();
            java.util.Date date = new java.util.Date(millis);
            List<Account> accounts = AccountDAO.list();
            accounts.add(newAccount);
            AccountDAO.save(accounts);
            for (Account account : getAccounts()) {
                if (account.getAccountNumber().equals(newAccount.getAccountNumber())) {
                    account.createTransaction(new Transaction(Transaction.TransactionType.DEPOSIT, newAccount.getAccountNumber(), date, newAccount.getBalance(), true));
                }
            }
        } catch (IOException e) {
            System.out.println("Khong tim thay file AccountDAO");
        }
    }
//    tinh tong so du cua khach hang
    public double getSum(){
        double sum = 0;
        for(Account account: getAccounts()){
            sum += account.getBalance();
        }
        return sum;
    }
//    xac dinh khach hang Premium hay khong
    public boolean isPremium() {
        for (Account account : getAccounts()) {
            if (account.isAccountPremium()) {
                return true;
            }
        }
        return false;
    }
//    lay account trong danh sach cac account
    public Account getAccountByAccountNumber(String accountNumber) {
        List<Account>accounts = AccountDAO.list();
        Account returnAccount = null;
        for (Account account : accounts) {
            if (account.getAccountNumber().equals(accountNumber)) {
                returnAccount = account;
            }
        }
        return returnAccount;
    }
//    lay so account trong 1 customer
    public Account getAccountInCustomer(String accountNumber) {
        Account returnAccount = null;
        for (Account account : getAccounts()) {
            if (account.getAccountNumber().equals(accountNumber)) {
                returnAccount = account;
            }
        }
        return returnAccount;
    }
//    hien thi thong tin khach hang va tai khoan
    public void displayInformation() {
        String isPre;
        if(isPremium())
            isPre="Premium";
        else
            isPre="Normal";
        System.out.printf("%-10s | %-15s  | %-7s | %,19.0f đ %n", getCustomerId(), getName(), isPre, getSum());
        for (int i = 0; i < this.getAccounts().size(); i++) {
            if (getAccounts().get(i) instanceof SavingsAccount) {
                System.out.println((i + 1) + getAccounts().get(i).toString("SAVINGS"));
            }
        }
    }
//    hien thi lich su giao dich
    public void displayTransactionInformation() {
        displayInformation();
        for (Account account : getAccounts()) {
            account.displayTransactionList();
        }
    }
//    kiem tra tai khoan co ton tai khong
    private boolean checkAccount(String accountNumber){
        List<Account>accounts = AccountDAO.list();
        for (Account account : accounts) {
            if (account.getAccountNumber().compareTo(accountNumber) == 0)
                return true;
        }
        return false;
    }
//    chuyen tien
    public void transfers(Scanner scanner) {
        while (true) {
            System.out.println("Nhap so tai khoan gui: ");
            String sendAccountNumber = scanner.nextLine();
            SavingsAccount sendAccount = (SavingsAccount) getAccountInCustomer(sendAccountNumber);
            if (validAccount(sendAccountNumber) && sendAccount != null) {
                while (true) {
                    System.out.println("Nhap so tai khoan nhan: ");
                    String receiveAccountNumber = scanner.nextLine();
                    boolean isReceiveAccountExits = checkAccount(receiveAccountNumber);
                    SavingsAccount receiveAccount = (SavingsAccount) getAccountByAccountNumber(receiveAccountNumber);
                    if (receiveAccountNumber.equals(sendAccountNumber)) {
                        System.out.println("Tai khoan nhan trung voi tai khoan gui");
                    } else if (!isReceiveAccountExits){
                        System.out.println("Tai khoan nhan khong ton tai ");
                    } else {
                        String comfirm;
                        double amount = inputAmount(sendAccount);
                        while (true) {
                            try {
                                System.out.printf("Xac nhan thuc hien chuyen %,1.0f đ tu tai khoan [%s] den tai khoan [%s] (Y/N): ", amount, sendAccountNumber, receiveAccountNumber);
                                comfirm = scanner.nextLine();
                                if (comfirm.equalsIgnoreCase("y")) {
                                    ((ITransfer) sendAccount).transfer(receiveAccount, amount);
                                    sendAccount.transferSavingsTransaction(true, receiveAccount, amount);
                                    AccountDAO.update(sendAccount);
                                    AccountDAO.update(receiveAccount);
                                    ((IReport) sendAccount).logTransfers(amount);
                                    break;
                                } else if (comfirm.equalsIgnoreCase("n")) {
                                    sendAccount.transferSavingsTransaction(false, receiveAccount, amount);
                                    break;
                                } else {
                                    System.out.println("Chi nhap 'y' hoac 'n'");
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        break;
                    }
                }
                break;
            } else if (sendAccount == null) {
                System.out.println("Tai khoan gui khong ton tai ");
            } else {
                System.out.println("So tai khoan khong hop le");
            }
        }
    }
//    rut tien
    public void withdraw(String withdrawAccount, double amount) {
        try {
            Account account = getAccountByAccountNumber(withdrawAccount);
            ((IWithdraw) account).withdraw(amount);
            if (account instanceof SavingsAccount ) {
                ((SavingsAccount) account).withdrawSavingsTransaction(true, amount);
                AccountDAO.update(account);
            }
            ((IReport) account).logWithdraw(amount);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
//    kiem tra so tien rut/chuyen hop le khong
    public double inputAmount(Account withdrawAccount) {
        double amount;
        boolean isValidAmount;
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("Nhap so tien muon rut/chuyen: ");
            String input = scanner.nextLine();
            try {
                isValidAmount = Double.parseDouble(input) >= 50000 && Double.parseDouble(input) % 10000 == 0;
                amount = Double.parseDouble(input);
                if (!isValidAmount) {
                    System.out.println("So tien rut/chuyen it nhat la 50.000 va la boi so cua 10.000");
                } else {
                    if (withdrawAccount instanceof SavingsAccount) {
                        if (((SavingsAccount) withdrawAccount).isAccepted(amount)) {
                            return amount;
                        } else if (Double.parseDouble(input) > withdrawAccount.getBalance()){
                            System.out.println("So tien rut/chuyen lon hon so du tai khoan");
                        } else if (withdrawAccount.getBalance() - amount <= 50000) {
                            System.out.println("So du tai khoan khong du 50.000");
                        } else {
                            System.out.println("So tien nhap khong hop le. Tai khoan Normal chi duoc rut/chuyen toi da 5tr/ lan.");
                        }
                    }
                }
            } catch (Exception e) {
                System.out.println("So tien nhap khong hop le. Vui long nhap lai");
            }
        }
    }
}
