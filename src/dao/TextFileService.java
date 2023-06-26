package dao;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TextFileService {
    private static final String COMMA_DELIMITER = ",";
//    doc file customers.txt
    public static List<List<String>> read(String fileName) {
        List<List<String>> customerList = new ArrayList<>();
        try (BufferedReader file = new BufferedReader((new FileReader(fileName)))) {
            String line;
            while ((line = file.readLine()) != null) {
                String[] splitData = line.split(COMMA_DELIMITER);
                if (splitData.length == 2) {
                    String customerId = splitData[0];
                    String name = splitData[1];
                    List<String> customer = Arrays.asList(customerId, name);
                    customerList.add(customer);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return customerList;
    }
}
