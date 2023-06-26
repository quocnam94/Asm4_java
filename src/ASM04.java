import dao.CustomerDAO;
import model.ASM2.Customer;
import model.DigitalBank;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Scanner;

public class ASM04 {
    private static final Scanner scanner = new Scanner(System.in);
    private static final DigitalBank activeBank = new DigitalBank();

    public static void main(String[] args) {
        menu();
        select();
    }
    public static void menu() {
        String AUTHOR = "FX20225";
        String VERSION = "V4.0.0";
        System.out.println("+----------+--------------------+----------+");
        System.out.println("|  NGAN HANG SO  |  " + AUTHOR + "@" + VERSION + "         |");
        System.out.println("+----------+--------------------+----------+");
        System.out.println("   1. Xem danh sach khach hang ");
        System.out.println("   2. Nhap danh sach khach hang ");
        System.out.println("   3. Them tai khoan ATM ");
        System.out.println("   4. Chuyen tien ");
        System.out.println("   5. Rut tien ");
        System.out.println("   6. Tra cuu lich su giao dich ");
        System.out.println("   0. Thoat ");
        System.out.println("+----------+--------------------+----------+");
    }
    //lua chon cac option
    public static void select() {
        System.out.print("Chuc nang: ");
        Scanner scanner;
        int n;
        try {
            while (true) {
                scanner = new Scanner(System.in);
                n = scanner.nextInt();
                switch (n) {
                    case 0 -> {
                        System.out.println("Chuong trinh ket thuc !!!");
                        System.exit(0);
                    }
                    case 1 -> showCustomer();
                    case 2 -> importCustomer();
                    case 3 -> addSaving();
                    case 4 -> transfer();
                    case 5 -> withdraw();
                    case 6 -> history();
                    default -> System.out.println("Vui long nhap so tu 0 toi 6");
                }
            }
        }
        catch (Exception InputMismatchException) {
            System.out.println("Vui long nhap so");
            select();
        }
    }
    //xem danh sach khach hang
    public static void showCustomer() {
        activeBank.showCustomers();
        select();
    }
    //nhap danh sach khach hang
    public static void importCustomer() {
        while (true) {
            System.out.println("Nhap duong dan den tep");
            String fileName = scanner.nextLine();
            Path path = FileSystems.getDefault().getPath(fileName);
            if (!Files.exists(path)) {
                System.out.println("Tep khong ton tai.");
            } else {
                activeBank.addCustomers(fileName);
                break;
            }
        }
        select();
    }
    //them tai khoan ATM
    public static void addSaving() {
        List<Customer> customers = CustomerDAO.list();
        if (customers.size() == 0) {
            System.out.println("Khong co khach hang trong danh sach");
        } else {
            activeBank.addSavingAccount(scanner);
        }
        select();
    }
    //chuyen tien
    public static void transfer() {
        List<Customer> customers = CustomerDAO.list();
        if (customers.size() == 0) {
            System.out.println("Khong co khach hang trong danh sach");
        } else {
            activeBank.transfer(scanner);
        }
        select();
    }
//    rut tien
    public static void withdraw() {
        List<Customer> customers = CustomerDAO.list();
        if (customers.size() == 0) {
            System.out.println("Khong co khach hang trong danh sach");
        } else {
            activeBank.withdraw(scanner);
        }
        select();
    }
    // xem lich su giao dich
    public static void history() {
        List<Customer> customers = CustomerDAO.list();
        if (customers.size() == 0) {
            System.out.println("Khong co khach hang trong danh sach");
        } else {
            activeBank.displayTransaction();
        }
        select();
    }
}