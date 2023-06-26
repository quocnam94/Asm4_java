package dao;

import model.ASM2.Customer;
import java.io.IOException;
import java.util.List;

public class CustomerDAO {
    public final static String FILE_PATH = "src/store/customers.dat";
    //     luu file customers.dat
    public static void save(List<Customer> customers) throws IOException {
        BinaryFileService.write(FILE_PATH, customers);
    }
    //     doc file customers.dat
    public static List<Customer> list() {
        return BinaryFileService.read(FILE_PATH);
    }
}
