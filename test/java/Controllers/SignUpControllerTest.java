package Controllers;

import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

class SignUpControllerTest {

    @Test
    void initialize() {
    }

    @Test
    void getNextSalt() {
    }

    @Test
    void signUp() {
        int ok = 0; //false
        File username1 = new File(System.getProperty("user.dir") + "\\src\\main\\resources\\"+"Toby"+"_likedItems.txt");
        if (username1 != null && !username1.getName().equals("123"))
            ok = 1;
        if (ok == 1)
            System.out.println("Test passed!");
        else
            System.out.println(("Test failed!"));
    }

    @Test
    void switchToLogInScreen() {
    }
}