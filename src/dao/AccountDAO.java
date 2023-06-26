package dao;

import model.ASM2.Account;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
public class AccountDAO {
    public static final String FILE_PATH = "src/store/accounts.dat";
//     luu file accounts.dat
    public static void save(List<Account> accounts) throws IOException {
        BinaryFileService.write(FILE_PATH, accounts);
    }
//    doc file accounts.dat
    public static List<Account> list() {
        return BinaryFileService.read(FILE_PATH);
    }
// cap nhat file accounts.dat
    public static void update(Account editAccount) throws IOException {
        List<Account> accounts = list();
        boolean hasExist = accounts.stream()
                .anyMatch(account -> account.getAccountNumber().equals(editAccount.getAccountNumber()));
        List<Account> updateAccounts;
        updateAccounts = new ArrayList<>();
        if (!hasExist) {
            updateAccounts = new ArrayList<>(accounts);
            updateAccounts.add(editAccount);
        } else {
            for (Account account : accounts) {
                if (account.getAccountNumber().equals(editAccount.getAccountNumber())) {
                    updateAccounts.add(editAccount);
                } else {
                    updateAccounts.add(account);
                }
            }
        }save(updateAccounts);
    }
}
