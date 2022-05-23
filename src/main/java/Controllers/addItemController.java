package Controllers;

import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.input.DragEvent;
import javafx.scene.input.TransferMode;

import javax.imageio.ImageIO;
import java.io.*;
import java.net.URL;
import java.util.List;

import javafx.scene.control.TextField;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.util.ResourceBundle;
import java.util.Scanner;

import static Controllers.Controller.*;

public class addItemController implements Initializable
{
    @FXML
    Label itemLabel;

    @FXML
    TextArea item_description;

    @FXML
    TextField item_name;

    @FXML
    ImageView imageView;

    String username=getUsername();

    private static Parent root;
    private static Stage stage;
    private static Scene scene;

    public Image imageToBeSaved;

    public void initialize(URL arg0, ResourceBundle arg1) {
    }

    @FXML
    private void handleDragOver(javafx.scene.input.DragEvent event)
    {
        if (event.getDragboard().hasFiles()) {
            event.acceptTransferModes(TransferMode.ANY);
        }
    }

    @FXML
    private void handleDrop(javafx.scene.input.DragEvent event)throws IOException
    {
        List<File> files = event.getDragboard().getFiles();
        Image img = new Image(new FileInputStream(files.get(0)));
        imageView.setImage(img);
        imageToBeSaved = imageView.getImage();
    }

    @FXML
    private void saveButton(javafx.event.ActionEvent event) throws FileNotFoundException {
        int present = 0;
        File file = new File(System.getProperty("user.dir") + "\\src\\main\\resources\\" + username + "_items.txt");
        Scanner reader = new Scanner(file);

        while (reader.hasNextLine()) {
            String data = reader.nextLine();
            String[] tok = data.split(",");
            if (tok[0].equals(item_name.getText())==true) {
                present = 1;
            }
        }

        if (present == 0) {
            String name, description;

            role = getRole();
            name = item_name.getText();
            description = item_description.getText();
            if (name.equals("") == false && description.equals("") == false) {
                if (imageToBeSaved != null) {
                    if (name.contains(" ") == false) {
                        if (description.contains(",") == false && name.contains(",") == false) {
                            file = new File(System.getProperty("user.dir") + "\\photos\\" + username + "_" + name + ".jpg");

                            try {
                                ImageIO.write(SwingFXUtils.fromFXImage(imageToBeSaved, null), "jpg", file);

                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            try {
                                file = new File(System.getProperty("user.dir") + "\\src\\main\\resources\\" + username + "_items.txt");

                                if (file.exists() == false) {
                                    file.createNewFile();
                                }

                                FileWriter writer = new FileWriter(file, true);
                                writer.write(name + "," + description + "," + username + "_" + name + ".jpg\n");
                                writer.close();

                                if (role == "user") {
                                    file = new File(System.getProperty("user.dir") + "\\src\\main\\resources\\unapproved_items.txt");
                                } else {
                                    file = new File(System.getProperty("user.dir") + "\\src\\main\\resources\\all_items.txt");
                                }
                                writer = new FileWriter(file, true);
                                writer.write(name + "," + description + "," + username + "_" + name + ".jpg\n");
                                writer.close();

                                if (role == "admin") {
                                    switchToLoggedInScreen_admin(event);
                                } else if (role == "user") {
                                    switchToLoggedInScreen_user(event);
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else {
                            itemLabel.setText("Name and description can't contain commas.");
                        }
                    } else {
                        itemLabel.setText("Name can't contain spaces.");
                    }
                } else {
                    itemLabel.setText("You need to provide a jpg image.");
                }
            } else {
                itemLabel.setText("Name and description can't be empty.");
            }
        }
        if(present == 1)
        {
            itemLabel.setText("Item with that name already exists.");
        }
    }

    @FXML
    private void cancelButton(javafx.event.ActionEvent event)
    {
        try
        {
            if(role=="admin")
            {
                switchToLoggedInScreen_admin(event);
            }

            else if(role=="user")
            {
                switchToLoggedInScreen_user(event);
            }
        }catch(IOException e)
        {
            e.printStackTrace();
        }
    }

    public void switchToLoggedInScreen_user(javafx.event.ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getClassLoader().getResource("fxmls/logged-in_user.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void switchToLoggedInScreen_admin(javafx.event.ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getClassLoader().getResource("fxmls/logged-in_admin.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }


}
