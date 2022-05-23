package Controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.Scanner;

public class SignUpController implements Initializable
{
    @FXML
    ImageView logo = new ImageView();
    URL url= new File(System.getProperty("user.dir") + "\\photos\\logo.png").toURI().toURL();
    javafx.scene.image.Image profile = new Image(url.toString());

    public void initialize(URL arg0, ResourceBundle arg1)
    {
        logo.setImage(profile);
    }

    @FXML
    Label myLabel;

    @FXML
    TextField tfs_username;

    @FXML
    PasswordField pfs_password;

    public String role;

    private Parent root;
    private Stage stage;
    private Scene scene;
    private static final Random RANDOM = new SecureRandom();

    public SignUpController() throws MalformedURLException {
    }

    public static String getNextSalt() {
        byte[] salt = new byte[16];
        RANDOM.nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);

    }

    public void signUp(javafx.event.ActionEvent event) throws FileNotFoundException
    {
        String username=tfs_username.getText();
        String password=pfs_password.getText();

        int ok=1;

        if(username.contains(" ")==true || password.contains(" ")==true)
        {
            ok=0;
            myLabel.setText("Name and password can't contain spaces.");
        }

        if(username.isEmpty() || password.isEmpty())
        {
            ok=0;
            myLabel.setText("Can't leave the input fields empty.");
        }

        File file = new File(System.getProperty("user.dir") + "\\src\\main\\resources\\Database.txt");
        Scanner reader = new Scanner(file);

        if(ok==1)
        {
            while (reader.hasNextLine())
            {
                String data = reader.nextLine();
                String[] tok = data.split(" ");

                String temp=username.hashCode()+"";

                if (tok[1].equals(temp))
                {
                    ok = 0;
                }
            }
        }

        if(ok==1)
        {

            try
            {
                FileWriter writer = new FileWriter(file, true);
                String salt = SignUpController.getNextSalt();

                if(username.contains("@admin.troc")==true)
                {
                    role="admin";
                }

                else
                {
                    role="user";
                }

                password+=salt;
                writer.write(salt+" "+username.hashCode()+" "+password.hashCode()+" "+role.hashCode()+"\n");
                writer.close();
            }catch(IOException e)
            {
                e.printStackTrace();
            }

            try {
                switchToLogInScreen(event);
            }catch(IOException e)
            {
                e.printStackTrace();
            }

            try {
                File newFile = new File(System.getProperty("user.dir") + "\\src\\main\\resources\\"+username+"_items.txt");
                newFile.createNewFile();

                File newFile2 = new File(System.getProperty("user.dir") + "\\src\\main\\resources\\"+username+"_likedItems.txt");
                newFile2.createNewFile();
            }catch(IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    public void switchToLogInScreen(javafx.event.ActionEvent event) throws IOException
    {
        root = FXMLLoader.load(getClass().getClassLoader().getResource("fxmls/sample.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

}
