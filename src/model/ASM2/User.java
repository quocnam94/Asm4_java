package model.ASM2;

import java.util.regex.Pattern;

public class User {
    public User() {
    }
    //kiem tra ma khach hang hop le khong
    public static boolean validCustomerId(String customerID){
        Pattern pattern = Pattern.compile("^\\d{12}$");
        return pattern.matcher(customerID).find();
    }
}
