package model.ASM2;

import dao.CustomerDAO;
import java.util.List;
import java.util.Scanner;

public class Bank {
    //kiem tra gia tri so du khi tao tai khoan
    public double validAmount() {
        boolean isValidAmount = false;
        String amount;
        Scanner scanner = new Scanner(System.in);
        do {
            System.out.println("Nhap so du tai khoan");
            amount = scanner.nextLine();
            try {
                isValidAmount = Double.parseDouble(amount) >= 50000;
                if (!isValidAmount) {
                    System.out.println("So du tai khoan it nhat la 50000. Vui long nhap lai");
                } else {
                    System.out.println("Tao tai khoan thanh cong");
                    return Double.parseDouble(amount);
                }
            } catch (Exception e) {
                System.out.println("So du tai khoan khong hop le.");
            }
        } while (!isValidAmount);
        return Double.parseDouble(amount);
    }
    //lay thong tin khach hang tu ma khach hang
    public Customer getCustomerById(String customerId) {
        List<Customer> customerList = CustomerDAO.list();
        Customer result = null;
        for (Customer customer : customerList) {
            if (customer.getCustomerId().equals(customerId)) {
                result = customer;
                break;
            }
        }
        return result;
    }


}
