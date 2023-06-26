package dao;

import model.Transaction;
import java.util.List;

public class TransactionDAO {
    public static final String FILE_PATH = "src/store/transactions.dat";
    //     luu file transactions.dat
    public static void save(List<Transaction> transactions){
        BinaryFileService.write(FILE_PATH, transactions);
    }
    //     doc file transactions.dat
    public static List<Transaction> list() {
        return BinaryFileService.read(FILE_PATH);
    }
}
