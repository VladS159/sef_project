package Controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Scanner;

public class searchAdminController extends ListView<String> implements Initializable
{

    private static Parent root;
    private static Stage stage;
    private static Scene scene;

    @FXML
    ListView<String> myListView3;

    @FXML
    TextField searchBar;

    String[] searchedList = new String[64];

    public void Search(javafx.event.ActionEvent event) throws IOException {
        int i=0;
        String keyword = searchBar.getText();

        if(keyword.equals("")==false)
        {
            for(int j=0;j<searchedList.length;j++)
            {
                searchedList[j]=null;
            }

            try
            {
                File wholeFile = new File(System.getProperty("user.dir") + "\\src\\main\\resources\\all_items.txt");
                Scanner reader = new Scanner(wholeFile);

                while(reader.hasNextLine()) {
                    String data = reader.nextLine();
                    String[] toks = data.split(",");

                    if (toks[0].contains(keyword) || toks[1].contains(keyword)) {
                        searchedList[i++] = toks[0]+","+toks[1]+","+toks[2];
                    }
                }

                myListView3.getItems().clear();
                myListView3.getItems().addAll(searchedList);

            }catch(IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    class Cell extends ListCell<String>
    {
        HBox hbox = new HBox();

        ImageView img = new ImageView();

        Label label = new Label("");
        Pane pane = new Pane();

        public Cell()
        {
            super();

            hbox.getChildren().addAll(img, label, pane);
            hbox.setHgrow(pane, Priority.ALWAYS);
        }

        public void updateItem(String name, boolean empty)
        {
            URL url = null;

            super.updateItem(name, empty);
            setText(null);
            setGraphic(null);

            if(name != null && !empty)
            {
                String[] toks=name.split(",");

                label.setText(toks[0]+","+toks[1]);

                try {
                    url= new File(System.getProperty("user.dir") + "\\photos\\"+toks[2]).toURI().toURL();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }

                javafx.scene.image.Image profile = new Image(url.toString(), 40, 40, false, false);
                img.setImage(profile);

                label.setText(toks[0]+","+toks[1]);
                setGraphic(hbox);
            }
        }
    }

    public void initialize(URL arg0, ResourceBundle arg1)
    {
        GridPane pane = new GridPane();
        Label name = new Label("h");


        pane.add(name, 0, 0);

        myListView3.setCellFactory(param -> new Cell());
    }

    public void adminItems(javafx.event.ActionEvent event) throws IOException
    {
        root = FXMLLoader.load(getClass().getClassLoader().getResource("fxmls/admin-items.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
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

    public void switchToLogInScreen(javafx.event.ActionEvent event) throws IOException
    {
        root = FXMLLoader.load(getClass().getClassLoader().getResource("fxmls/sample.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }


    public void switchToAddItemScreen(javafx.event.ActionEvent event) throws IOException
    {
        root = FXMLLoader.load(getClass().getClassLoader().getResource("fxmls/add-item-page.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

}
