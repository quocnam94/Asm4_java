package model;

import CustomerIdNotValidException.CustomerIdNotValidException;
import dao.CustomerDAO;
import dao.TextFileService;
import model.ASM2.Account;
import model.ASM2.Bank;
import model.ASM2.Customer;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;
import static model.ASM2.Account.validAccount;
import static model.ASM2.User.validCustomerId;
public class DigitalBank extends Bank {
    //doc va hien thi thong tin khach hang
    public void showCustomers() {
        List<Customer> customers = CustomerDAO.list();
        if (customers.size() == 0) {
            System.out.println("Chua co khach hang nao trong danh sach");
        } else {
            for (Customer customer : customers) {
                customer.displayInformation();
            }
        }
    }
    //them khach hang
    public void addCustomers(String fileName) {
        List<List<String>> result = new ArrayList<>();
        List<Customer> customers = CustomerDAO.list();
        List<List<String>> customerList = TextFileService.read(fileName);
        try {
            for (List<String> importCustomer : customerList) {
                if (importCustomer.size() != 2) {
                    System.out.println("Dinh dang so CCCD va ten chua hop le, them khach hang khong thanh cong");
                } else if (!validCustomerId(importCustomer.get(0))) {
                    System.out.println("So CCCD khong hop le, them khach hang khong thanh cong");
                } else {
                    Customer newCustomer = new Customer(importCustomer);
                    if (isCustomerExisted(customers, newCustomer)) {
                        System.out.println("Khach hang " + importCustomer.get(0) + " da ton tai, them khach hang khong thanh cong");
                    } else {
                        customers.add(newCustomer);
                        System.out.println("Da them khach hang " + importCustomer.get(0) + " vao danh sach khach hang");
                        CustomerDAO.save(customers);
                    }
                }
                result.add(importCustomer);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //them tai khoan ATM
    public void addSavingAccount(Scanner scanner) {
        List<Customer> customers = CustomerDAO.list();
        boolean isValidCustomerId;
        do {
            System.out.println("Nhap ma so khach hang: ");
            String customerId = scanner.nextLine();
            isValidCustomerId = true;
            boolean isExistCustomer = checkCustomer(customerId);
            if (!validCustomerId(customerId)) {
                System.out.println("Ma so khach hang khong hop le");
                isValidCustomerId = false;
            } else if (!isExistCustomer) {
                System.out.println("Khong tim thay khach hang " + customerId + ", tac vu khong thanh cong.");
            } else {
                boolean isValidAccount = false;
                Customer customer = getCustomerById(customers, customerId);
                while (!isValidAccount) {
                    System.out.println("Nhap so tai khoan gom 6 chu so: ");
                    String inputAccount = scanner.nextLine();
                    if (!validAccount(inputAccount)) {
                        System.out.println("So tai khoan khong hop le");
                    } else {
                        if (isAccountExisted(customers, inputAccount)) {
                            System.out.println("So tai khoan da ton tai. Vui long nhap so tai khoan khac");
                        } else {
                            isValidAccount = true;
                            double balance = validAmount();
                            SavingsAccount savingsAccount = new SavingsAccount(customerId, inputAccount, balance);
                            customer.addAccount(savingsAccount);
                        }
                    }
                }
            }
        } while (!isValidCustomerId);
    }
//    chuyen tien
    public void transfer(Scanner scanner) {
        while (true) {
            try {
                System.out.println("Nhap ma so khach hang muon chuyen tien: ");
                String sendTransfer = scanner.nextLine();
                boolean isValidSendTransfer = validCustomerId(sendTransfer);
                boolean isExistCustomer = checkCustomer(sendTransfer);
                if (!isValidSendTransfer) {
                    System.out.println("Ma so khach hang khong hop le");
                } else if (!isExistCustomer) {
                    System.out.println("Khong tim thay khach hang " + sendTransfer + ", tac vu khong thanh cong.");
                } else {
                    Customer sendCustomer = getCustomerById(sendTransfer);
                    sendCustomer.displayInformation();
                    sendCustomer.transfers(scanner);
                    break;
                }
            } catch (CustomerIdNotValidException e) {
                throw new CustomerIdNotValidException("Ma so khach hang khong ton tai");
            }
        }
    }
//    rut tien
    public void withdraw(Scanner scanner) {
        while (true) {
            System.out.println("Nhap ma so khach hang muon rut tien: ");
            String customerId = scanner.nextLine();
            List<Customer> customers = CustomerDAO.list();
            Customer customer = getCustomerById(customers, customerId);
            if (!validCustomerId(customerId)) {
                System.out.println("Ma so khach hang khong hop le.");
            } else if (customer == null) {
                System.out.println("Ma so khach hang khong ton tai.");
            } else {
                List<Account> accounts = customer.getAccounts();
                if (accounts.size() == 0) {
                    System.out.println("Khach hang khong co tai khoan. Vui long nhap lai");
                    break;
                } else {
                    try {
                        customer.displayInformation();
                        while (true) {
                            System.out.println("Nhap so tai khoan: ");
                            String inputAccount = scanner.nextLine();
                            if (validAccount(inputAccount)) {
                                Account account = customer.getAccountInCustomer(inputAccount);
                                if (account == null) {
                                    System.out.println("Khach hang khong co so tai khoan nay. Vui long nhap lai ");
                                } else {
                                    double amount = customer.inputAmount(account);
                                    customer.withdraw(inputAccount, amount);
                                    break;
                                }
                            } else {
                                System.out.println("So tai khoan khong hop le. Vui long nhap lai ");
                            }
                        }
                        break;
                    } catch (Exception e) {
                        System.out.println("Ma so khach hang khong hop le. Vui long nhap lai");
                    }
                }
            }
        }
    }
// xem lich su giao dich
    public void displayTransaction() {
        Scanner scanner = new Scanner(System.in);
        List<Customer> customers = CustomerDAO.list();
        boolean isValidCustomerId;
        while (true) {
            System.out.println("Nhap ma so khach hang muon xem lich su giao dich: ");
            String customerId = scanner.nextLine();
            try {
                isValidCustomerId = validCustomerId(customerId);
                Customer customer = getCustomerById(customers, customerId);
                if (!isValidCustomerId) {
                    System.out.println("Ma so khach hang khong hop le");
                } else if (getCustomerById(customerId) == null) {
                    System.out.println("Ma so khach hang khong ton tai");
                } else {
                    List<Account> accounts = customer.getAccounts();
                    if (accounts.size() == 0) {
                        System.out.println("Khach hang khong co tai khoan");
                    }
                    customer.displayTransactionInformation();
                    break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
//    kiem tra khach hang moi co trung khach hang cu khong
    private boolean isCustomerExisted(List<Customer> customers, Customer newCustomer) {
        return customers.stream()
                .anyMatch(customer -> customer.getCustomerId().equals(newCustomer.getCustomerId()));
    }
//    kiem tra so tai khoan co ton tai khong
    public static boolean isAccountExisted(List<Customer> customers, String accountNumber){
        for (Customer customer : customers) {
            for (int i = 0; i < customer.getAccounts().size(); i++) {
                Account account = customer.getAccounts().get(i);
                if (Objects.equals(accountNumber, account.getAccountNumber()))
                    return true;
            }
        }
        return false;
    }
//    kiem tra khach hang co ton tai khong
    private boolean checkCustomer(String customerId){
        List<Customer>customers = CustomerDAO.list();
        for (Customer customer : customers) {
            if (customer.getCustomerId().compareTo(customerId) == 0)
                return true;
        }
        return false;
    }
//    lay ra khach hang tu ma khach hang
    public Customer getCustomerById(List<Customer> customerList, String customerId) {
        Customer customer = null;
        for (Customer cus : customerList) {
            if (cus.getCustomerId().equals(customerId)) {
                customer = cus;
            }
        }
        return customer;
    }
}
